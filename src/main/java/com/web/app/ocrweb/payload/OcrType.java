package com.web.app.ocrweb.payload;

public enum OcrType {
    QR("Mã QR", "QR Code"),
    OCR("Nhận dạng ký tự", "Optical Character Recognition");

    private final String descriptionVi; // Mô tả tiếng Việt
    private final String descriptionEn; // Mô tả tiếng Anh

    // Constructor để khởi tạo mô tả cho mỗi giá trị
    OcrType(String descriptionVi, String descriptionEn) {
        this.descriptionVi = descriptionVi;
        this.descriptionEn = descriptionEn;
    }

    // Getter để lấy mô tả tiếng Việt
    public String getDescriptionVi() {
        return descriptionVi;
    }

    // Getter để lấy mô tả tiếng Anh
    public String getDescriptionEn() {
        return descriptionEn;
    }

    // Phương thức để tìm kiếm enum theo mô tả tiếng Việt hoặc tiếng Anh
    public static OcrType fromDescription(String description) {
        for (OcrType ocrType : OcrType.values()) {
            if (ocrType.getDescriptionVi().equalsIgnoreCase(description) ||
                    ocrType.getDescriptionEn().equalsIgnoreCase(description)) {
                return ocrType;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy OcrType cho mô tả: " + description);
    }

    @Override
    public String toString() {
        return descriptionVi + " (" + descriptionEn + ")";
    }
}
