package com.web.app.ocrweb.payload;

import java.util.List;

// {

//     "citizenIdCard": {
//       "citizenCode": "123456789012",
//       "fullName": "Nguyen Van A",
//       "dateOfBirth": "1990-01-01",
//       "gender": "Male",
//       "address": "123 Nguyen Trai, Hanoi",
//       "issuedDate": "2020-05-10",
//       "expiryDate": "2030-05-10",
//       "cardType": "CCCD",
//       "isFront": true,
//       "ethnicity": "Kinh",
//       "religion": "Không",
//       "hometown": "Hà Nội",
//       "cmndOldCode": null
//     }
//   }

public class CitizenIdCard {
    private String citizenCode;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String address;
    private String issuedDate;
    private String expiryDate;
    private CardType cardType; // Use the enum here
    private CardFace cardFace;
    private String ethnicity;
    private String religion;
    private String hometown;
    private String cmndOldCode;
    private CardDate cardDate;
    private OcrType ocrType;

    private List<OcrResult> ocrResult;

    public CitizenIdCard() {

    }

    // Constructors, getters, and setters

    public CitizenIdCard(String citizenCode, String fullName, String dateOfBirth,
            String gender, String address, String issuedDate, String expiryDate,
            CardType cardType, CardFace cardFace, String ethnicity,
            String religion, String hometown, String cmndOldCode) {
        this.citizenCode = citizenCode;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.issuedDate = issuedDate;
        this.expiryDate = expiryDate;
        this.cardType = cardType; // Now using the enum

        this.ethnicity = ethnicity;
        this.religion = religion;
        this.hometown = hometown;
        this.cmndOldCode = cmndOldCode;
    }

    public String getCitizenCode() {
        return citizenCode;
    }

    public void setCitizenCode(String citizenCode) {
        this.citizenCode = citizenCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getCmndOldCode() {
        return cmndOldCode;
    }

    public void setCmndOldCode(String cmndOldCode) {
        this.cmndOldCode = cmndOldCode;
    }

    public CardFace getCardFace() {
        return cardFace;
    }

    public void setCardFace(CardFace cardFace) {
        this.cardFace = cardFace;
    }

    public CardDate getCardDate() {
        return cardDate;
    }

    public void setCardDate(CardDate cardDate) {
        this.cardDate = cardDate;
    }

    public List<OcrResult> getOcrResult() {
        return ocrResult;
    }

    public void setOcrResult(List<OcrResult> ocrResult) {
        this.ocrResult = ocrResult;
    }

    public OcrType getOcrType() {
        return ocrType;
    }

    public void setOcrType(OcrType ocrType) {
        this.ocrType = ocrType;
    }
    
}
