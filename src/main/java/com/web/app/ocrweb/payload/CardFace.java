package com.web.app.ocrweb.payload;
  // Giả sử bạn có OcrResult là một class chứa kết quả OCR
import java.util.List;

public enum CardFace {
    FRONT_VI("Mặt trước", "Front"),     // Mặt trước (tiếng Việt và tiếng Anh)
    BACK_VI("Mặt sau", "Back"),         // Mặt sau (tiếng Việt và tiếng Anh)
    UNDEFINED_VI("Không xác định", "Undefined"), // Không xác định
    INVALID_VI("Không đúng form card", "Invalid card format"); // Không đúng form card

    private final String descriptionVi;  // Mô tả tiếng Việt
    private final String descriptionEn;  // Mô tả tiếng Anh

    // Constructor để khởi tạo mô tả cho mỗi giá trị
    CardFace(String descriptionVi, String descriptionEn) {
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
    public static CardFace fromDescription(String description) {
        for (CardFace face : CardFace.values()) {
            if (face.getDescriptionVi().equalsIgnoreCase(description) || 
                face.getDescriptionEn().equalsIgnoreCase(description)) {
                return face;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy CardFace cho mô tả: " + description);
    }

    // Phương thức xác định mặt của căn cước từ kết quả OCR và loại thẻ
    public static CardFace detectCardFace(List<OcrResult> ocrResults, CardType cardType) {
        boolean hasFrontKeywords = false;
        boolean hasBackKeywords = false;

        // Các từ khóa cho từng loại thẻ
        String[] frontKeywords = getFrontKeywords(cardType);
        String[] backKeywords = getBackKeywords(cardType);

        // Kiểm tra trong các kết quả OCR để xác định mặt thẻ
        for (OcrResult result : ocrResults) {
            String text = result.getText().toLowerCase(); // Chuyển văn bản thành chữ thường để so sánh

            // Kiểm tra mặt trước
            for (String keyword : frontKeywords) {
                if (text.contains(keyword.toLowerCase())) {
                    hasFrontKeywords = true;
                    break;
                }
            }

            // Kiểm tra mặt sau
            for (String keyword : backKeywords) {
                if (text.contains(keyword.toLowerCase())) {
                    hasBackKeywords = true;
                    break;
                }
            }
        }

        // Xác định mặt căn cước
        if (hasFrontKeywords && !hasBackKeywords) {
            return FRONT_VI;  // Mặt trước
        } else if (hasBackKeywords && !hasFrontKeywords) {
            return BACK_VI;   // Mặt sau
        } else if (!hasFrontKeywords && !hasBackKeywords) {
            return UNDEFINED_VI;  // Không xác định
        } else {
            return INVALID_VI; // Không đúng form card (ví dụ: chứa cả hai từ khóa mà không xác định rõ mặt nào)
        }
    }

    // Phương thức lấy từ khóa mặt trước cho từng loại thẻ
    private static String[] getFrontKeywords(CardType cardType) {
        switch (cardType) {
            case CCCD:
                return new String[]{"Công dân", "Họ tên", "Số thẻ", "CCCD"};
            case CMND:
                return new String[]{"CMND", "Họ tên", "Số CMND"};
            case GPLX:
                return new String[]{"Giấy phép lái xe", "Số GPLX", "Họ tên"};
            default:
                return new String[]{};
        }
    }

    // Phương thức lấy từ khóa mặt sau cho từng loại thẻ
    private static String[] getBackKeywords(CardType cardType) {
        switch (cardType) {
            case CCCD:
                return new String[]{"Ngày cấp", "Ngày sinh", "Quốc tịch"};
            case CMND:
                return new String[]{"Ngày cấp", "Ngày sinh", "Quốc tịch"};
            case GPLX:
                return new String[]{"Ngày cấp", "Ngày hết hạn"};
            default:
                return new String[]{};
        }
    }
    @Override
    public String toString() {
        return    descriptionVi + " (" + descriptionEn + ")";
    }
}
