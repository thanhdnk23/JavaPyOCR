package com.web.app.ocrweb.config;

public enum Language {
    VIETNAMESE("Tiếng Việt", "vi"), // Tiếng Việt
    ENGLISH("English", "en"), // Tiếng Anh
    JAPANESE("日本語", "ja"), // Tiếng Nhật
    FRENCH("Français", "fr"), // Tiếng Pháp
    GERMAN("Deutsch", "de"), // Tiếng Đức
    SPANISH("Español", "es"), // Tiếng Tây Ban Nha
    CHINESE("中文", "zh"); // Tiếng Trung

    private final String name; // Tên ngôn ngữ (ví dụ: Tiếng Việt, English)
    private final String code; // Mã ngôn ngữ (ví dụ: "vi", "en", "ja")

    // Constructor để khởi tạo enum với tên ngôn ngữ và mã ngôn ngữ
    Language(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // Getter để lấy tên ngôn ngữ
    public String getName() {
        return name;
    }

    // Getter để lấy mã ngôn ngữ
    public String getCode() {
        return code;
    }

    // Phương thức để tìm kiếm ngôn ngữ từ mã ngôn ngữ
    public static Language fromCode(String code) {
        for (Language lang : Language.values()) {
            if (lang.getCode().equalsIgnoreCase(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy ngôn ngữ cho mã: " + code);
    }

    // Phương thức để tìm kiếm ngôn ngữ từ tên ngôn ngữ
    public static Language fromName(String name) {
        for (Language lang : Language.values()) {
            if (lang.getName().equalsIgnoreCase(name)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy ngôn ngữ cho tên: " + name);
    }
}
