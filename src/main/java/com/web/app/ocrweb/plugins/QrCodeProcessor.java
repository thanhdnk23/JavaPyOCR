package com.web.app.ocrweb.plugins;
import java.awt.image.BufferedImage;
import java.util.List;

import com.web.app.ocrweb.payload.CardFace;
import com.web.app.ocrweb.payload.CardType;
import com.web.app.ocrweb.payload.CitizenIdCard;
import com.web.app.ocrweb.payload.ReturnObject;

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.ConfigQrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.filter.blur.BlurImageOps;

public class QrCodeProcessor {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ReturnObject detectQrCode(BufferedImage input) {
        // Chuyển đổi ảnh sang mức xám (grayscale)
        GrayU8 gray = ConvertBufferedImage.convertFrom(input, (GrayU8) null);

        // Bước 1: Làm mịn ảnh để giảm nhiễu
        GrayU8 smoothed = new GrayU8(gray.width, gray.height);
        BlurImageOps.gaussian(gray, smoothed, -1, 3, null);

        // Bước 2: Cân bằng histogram để tăng cường độ tương phản
        GrayU8 enhanced = smoothed.createSameShape();
        histogramEqualize(smoothed, enhanced);
        // Bước 3: Áp dụng ngưỡng hóa ảnh tự động với Otsu
        GrayU8 binary = new GrayU8(enhanced.width, enhanced.height);
        double otsuThreshold = GThresholdImageOps.computeOtsu(enhanced, 0, 255); // Tính ngưỡng Otsu
        GThresholdImageOps.threshold(enhanced, binary, (int) otsuThreshold, true); // Áp dụng ngưỡng Otsu

        // Khởi tạo bộ phát hiện QR code
        ConfigQrCode config = new ConfigQrCode();
        QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(config, GrayU8.class);
        detector.process(binary); // Sử dụng ảnh đã xử lý để phát hiện

        List<QrCode> detections = detector.getDetections();
        ReturnObject result = new ReturnObject();
        result.setStatus(-1); // Default status for error
        result.setSuccess(false);

        if (!detections.isEmpty()) {
            QrCode qr = detections.get(0);
            try {
                CitizenIdCard citizenIdCard = extractDataFromQRCode(qr.message);
                result.setStatus(0); // Success status
                result.setSuccess(true);
                result.setData(citizenIdCard);
            } catch (Exception e) {
                System.out.println(e);
                result.setMessage("Invalid QR code format");
                return result;
            }
        } else {
            result.setMessage("No QR codes detected");
        }

        return result;
    }
    private void histogramEqualize(GrayU8 input, GrayU8 output) {
        int[] histogram = new int[256];
        int[] cumulativeHistogram = new int[256];

        // Step 1: Calculate histogram
        for (int y = 0; y < input.height; y++) {
            for (int x = 0; x < input.width; x++) {
                int value = input.get(x, y);
                histogram[value]++;
            }
        }

        // Step 2: Calculate cumulative histogram
        cumulativeHistogram[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cumulativeHistogram[i] = cumulativeHistogram[i - 1] + histogram[i];
        }

        // Step 3: Normalize cumulative histogram to create a lookup table
        int totalPixels = input.width * input.height;
        int[] lookupTable = new int[256];
        for (int i = 0; i < 256; i++) {
            lookupTable[i] = (cumulativeHistogram[i] * 255) / totalPixels;
        }

        // Step 4: Apply the equalization using the lookup table
        for (int y = 0; y < input.height; y++) {
            for (int x = 0; x < input.width; x++) {
                int value = input.get(x, y);
                output.set(x, y, lookupTable[value]);
            }
        }
    }
    private CitizenIdCard extractDataFromQRCode(String qrMessage) throws Exception {
        String[] parts = qrMessage.split("\\|");
        if (parts.length < 7)
            throw new IllegalArgumentException("Invalid QR data");

        CitizenIdCard citizenIdCard = new CitizenIdCard();
        citizenIdCard.setCitizenCode(parts[0]);
        citizenIdCard.setCmndOldCode(parts[1]);
        citizenIdCard.setFullName(parts[2]);
        citizenIdCard.setDateOfBirth(parts[3]);
        citizenIdCard.setGender(parts[4]);
        citizenIdCard.setAddress(parts[5]);
        citizenIdCard.setIssuedDate(parts[6]);
        citizenIdCard.setCardType(CardType.CCCD);

        // Set default values for other fields, or modify based on your needs
        citizenIdCard.setCardFace(CardFace.FRONT_VI); // Assuming it is the front side
        citizenIdCard.setExpiryDate(null); // Adjust based on your logic
        citizenIdCard.setEthnicity("Unknown"); // Example value
        citizenIdCard.setReligion("Unknown"); // Example value
        citizenIdCard.setHometown("Unknown"); // Example value

        return citizenIdCard;
    }

}
