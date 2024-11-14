package com.web.app.ocrweb.config;

public enum OpenCVConfigEnum {
    WINDOWS("lib/opencv/window/opencv_java490.dll"),
    LINUX("lib/opencv/linux/libopencv_java490.so");

    private final String libraryPath;

    // Constructor enum lưu đường dẫn của thư viện OpenCV
    OpenCVConfigEnum(String libraryPath) {
        this.libraryPath = libraryPath;
    }

    // Getter để lấy đường dẫn thư viện
    public String getLibraryPath() {
        return libraryPath;
    }

    // Phương thức tĩnh để lấy cấu hình thư viện phù hợp với hệ điều hành
    public static OpenCVConfigEnum getConfigForCurrentOS() {
        String osName = System.getProperty("os.name").toLowerCase();

        // Kiểm tra xem hệ điều hành có phải là Windows hay không
        if (osName.contains("win")) {
            return WINDOWS;
        }
        // Kiểm tra xem hệ điều hành có phải là Linux hay không
        else if (osName.contains("nix") || osName.contains("nux")) {
            return LINUX;
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + osName);
        }
    }
}
