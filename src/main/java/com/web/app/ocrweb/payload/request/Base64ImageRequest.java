package com.web.app.ocrweb.payload.request;

public class Base64ImageRequest {
    private String base64Image;
    private int cardType;
    // Constructor, getter, setter
    public Base64ImageRequest() {

    }

    public Base64ImageRequest(String base64Image, int cardType) {
        this.base64Image = base64Image;
        this.cardType = cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getCardType() {
        return cardType;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
