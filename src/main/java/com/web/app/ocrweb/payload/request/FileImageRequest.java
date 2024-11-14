package com.web.app.ocrweb.payload.request;

import org.springframework.web.multipart.MultipartFile;

public class FileImageRequest {
    private MultipartFile fileImage;
    private int cardType;

    public FileImageRequest() {
    }

    public FileImageRequest(MultipartFile fileImage, int cardType) {
        this.fileImage = fileImage;
        this.cardType = cardType;
    }

    public MultipartFile getFileImage() {
        return fileImage;
    }

    public void setFileImage(MultipartFile fileImage) {
        this.fileImage = fileImage;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
