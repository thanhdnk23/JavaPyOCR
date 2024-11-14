package com.web.app.ocrweb.payload;

public enum CardType {
    CCCD(0, "CCCD", "Căn Cước Công Dân", "Citizen ID card"), // Citizen ID card
    CMND(1, "CMND", "Chứng Minh Nhân Dân", "Old ID card"), // Old ID card (CMND)
    GPLX(2, "GPLX", "Giấy Phép Lái Xe", "Driver's License"); // Driver's License

    private final int id;
    private final String code;
    private final String descriptionVi;
    private final String descriptionEn;

    // Constructor for the enum
    CardType(int id, String code, String descriptionVi, String descriptionEn) {
        this.id = id;
        this.code = code;
        this.descriptionVi = descriptionVi;
        this.descriptionEn = descriptionEn;
    }

    // Getters for each field
    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescriptionVi() {
        return descriptionVi;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    // Override toString to display both Vietnamese and English descriptions
    @Override
    public String toString() {
        return id + "-" + descriptionVi + " (" + descriptionEn + ")";
    }

    // Phương thức tìm kiếm enum theo ID
    public static CardType fromId(int id) {
        for (CardType type : CardType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CardType ID: " + id);
    }
}
