package com.web.app.ocrweb.pythonmodel;

public enum VietOcrModel {
    // Định nghĩa các hằng số cho các mô hình OCR
    VIETOCR_VGGSEQ2SEQ("vgg_seq2seq"),
    VIETOCR_TRANSFORMER("vgg_transformer"),


    // Thuộc tính để lưu trữ tên mô hình
    private final String modelName;
 
    // Constructor cho enum
    VietOcrModel(String modelName) {
        this.modelName = modelName;
    }

    // Phương thức để lấy tên mô hình
    public String getModelName() {
        return modelName;
    }

    // Phương thức để lấy mô hình từ tên
    public static VietOcrModel fromModelName(String modelName) {
        for (VietOcrModel model : VietOcrModel.values()) {
            if (model.getModelName().equalsIgnoreCase(modelName)) {
                return model;
            }
        }
        throw new IllegalArgumentException("No model found with name: " + modelName);
    }
}
