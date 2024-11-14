package com.web.app.ocrweb.plugins;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;

import com.web.app.ocrweb.payload.*;
import com.web.app.ocrweb.payload.request.Base64ImageRequest;
import com.web.app.ocrweb.payload.request.FileImageRequest;

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.ConfigQrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;

public class QrCodeProcessor_v1 {

    public CitizenIdCard detectQrCodeFileImage(FileImageRequest image) throws Exception {
        if (image.getCardType() == 0) {
            return detectQrCodeFromGray(convertToGray(image));
        } else {
            return processOcr(image.getFileImage().getInputStream(), CardType.CMND);
        }
    }

    public CitizenIdCard detectQrCodeBase64(Base64ImageRequest image) throws Exception {
        if (image.getCardType() == 0) {
            return detectQrCodeFromGray(convertToGray(image));
        } else {
            byte[] imageBytes = Base64.getDecoder().decode(image.getBase64Image());
            return processOcr(new java.io.ByteArrayInputStream(imageBytes), CardType.CMND);
        }
    }

    public ReturnObject detectQrCode(BufferedImage input) {
        CitizenIdCard citizenIdCard = detectQrCodeFromGray(ConvertBufferedImage.convertFrom(input, (GrayU8) null));
        ReturnObject result = new ReturnObject();
        result.setStatus(citizenIdCard != null ? 0 : -1);
        result.setSuccess(citizenIdCard != null);
        result.setData(citizenIdCard);
        return result;
    }

    private CitizenIdCard detectQrCodeFromGray(GrayU8 gray) {
        ConfigQrCode config = new ConfigQrCode();
        QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(config, GrayU8.class);
        detector.process(gray);

        List<QrCode> detections = detector.getDetections();
        if (!detections.isEmpty()) {
            try {
                return extractDataFromQRCode(detections.get(0).message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private CitizenIdCard processOcr(InputStream inputStream, CardType cardType) throws Exception {
        File tempFile = File.createTempFile("temp_image", ".jpg");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            inputStream.transferTo(fos);
        }
        List<OcrResult> ocrResults = OCRImageProcessor_v1.runOCRContainer(tempFile);
        CitizenIdCard citizenIdCard = new CitizenIdCard();
        citizenIdCard.setCardFace(CardFace.detectCardFace(ocrResults, cardType));
        citizenIdCard.setOcrResult(ocrResults);
        citizenIdCard.setOcrType(OcrType.OCR);
        tempFile.delete();
        return citizenIdCard;
    }

    private GrayU8 convertToGray(FileImageRequest image) throws Exception {
        return ConvertBufferedImage.convertFrom(
                ImageConverter.multipartFileToBufferedImage(image.getFileImage()), (GrayU8) null);
    }

    private GrayU8 convertToGray(Base64ImageRequest image) throws Exception {
        return ConvertBufferedImage.convertFrom(
                ImageConverter.base64ToBufferedImage(image.getBase64Image()), (GrayU8) null);
    }

    private CitizenIdCard extractDataFromQRCode(String qrMessage) throws Exception {
        String[] parts = qrMessage.split("\\|");
        if (parts.length < 7) throw new IllegalArgumentException("Invalid QR data");

        CitizenIdCard citizenIdCard = new CitizenIdCard();
        citizenIdCard.setCitizenCode(parts[0]);
        citizenIdCard.setCmndOldCode(parts[1]);
        citizenIdCard.setFullName(parts[2]);
        citizenIdCard.setDateOfBirth(parts[3]);
        citizenIdCard.setGender(parts[4]);
        citizenIdCard.setAddress(parts[5]);
        citizenIdCard.setIssuedDate(parts[6]);
        citizenIdCard.setCardType(CardType.CCCD);
        citizenIdCard.setOcrType(OcrType.QR);

        LocalDate dateOfBirth = tryParseDate(parts[3]);
        LocalDate issuedDate = tryParseDate(parts[6]);
        citizenIdCard.setDateOfBirth(formatDate(dateOfBirth));
        citizenIdCard.setIssuedDate(formatDate(issuedDate));
        citizenIdCard.setExpiryDate(formatDate(calculateNextRenewalDate(dateOfBirth, issuedDate)));

        citizenIdCard.setCardFace(CardFace.FRONT_VI);
        citizenIdCard.setEthnicity("Unknown");
        citizenIdCard.setReligion("Unknown");
        citizenIdCard.setHometown("Unknown");
        citizenIdCard.setCardDate(CardDate.getCardStatus(issuedDate, calculateNextRenewalDate(dateOfBirth, issuedDate)));
        return citizenIdCard;
    }

    private LocalDate tryParseDate(String dateString) {
        DateTimeFormatter[] formatters = { DateTimeFormatter.ofPattern("ddMMyyyy"), DateTimeFormatter.ofPattern("dd/MM/yyyy"), DateTimeFormatter.ofPattern("dd-MM-yyyy") };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Ignore and try next
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + dateString);
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
    }

    private LocalDate calculateNextRenewalDate(LocalDate dateOfBirth, LocalDate issuedDate) {
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        int nextMilestoneAge = age < 25 ? 25 : age < 40 ? 40 : age < 60 ? 60 : 9999;
        LocalDate nextRenewalDate = dateOfBirth.plusYears(nextMilestoneAge);
        return nextRenewalDate.isBefore(issuedDate) ? issuedDate.plusYears(nextMilestoneAge - (age % nextMilestoneAge)) : nextRenewalDate;
    }
}
