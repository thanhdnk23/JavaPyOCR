package com.web.app.ocrweb.payload;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum CardDate {
    VALID("Còn thời hạn", "Valid"), // Còn thời hạn
    EXPIRING_SOON("Sắp đến thời hạn", "Expiring Soon"), // Sắp đến thời hạn
    EXPIRED("Hết hạn", "Expired"), // Hết hạn
    INDEFINITE("Vô thời hạn", "Indefinite"), // Vô thời hạn
    UNDEFINED("Không xác định", "Undefined"); // Không xác định

    private final String descriptionVi; // Mô tả tiếng Việt
    private final String descriptionEn; // Mô tả tiếng Anh

    // Constructor để khởi tạo mô tả cho mỗi giá trị
    CardDate(String descriptionVi, String descriptionEn) {
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
    public static CardDate fromDescription(String description) {
        for (CardDate cardDate : CardDate.values()) {
            if (cardDate.getDescriptionVi().equalsIgnoreCase(description) ||
                    cardDate.getDescriptionEn().equalsIgnoreCase(description)) {
                return cardDate;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy CardDate cho mô tả: " + description);
    }

    // Phương thức xác định trạng thái thẻ dựa trên ngày bắt đầu và ngày kết thúc
    public static CardDate getCardStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = LocalDate.now(); // Lấy ngày hiện tại

        if (startDate == null || endDate == null) {
            return UNDEFINED;  // Nếu ngày bắt đầu hoặc kết thúc không xác định
        }

        if (startDate.isAfter(currentDate)) {
            // Nếu thẻ chưa bắt đầu
            return INDEFINITE; // Vô thời hạn nếu chưa có ngày bắt đầu
        }

        if (endDate.isBefore(currentDate)) {
            // Nếu thẻ đã hết hạn
            return EXPIRED;
        }

        long daysToExpire = ChronoUnit.DAYS.between(currentDate, endDate); // Tính số ngày còn lại đến hết hạn

        if (daysToExpire <= 30) {
            // Nếu còn dưới 30 ngày
            return EXPIRING_SOON;
        }

        // Nếu thẻ còn thời hạn
        return VALID;
    }
    @Override
    public String toString() {
        return    descriptionVi + " (" + descriptionEn + ")";
    }
}
