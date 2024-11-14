package com.web.app.ocrweb;
import java.io.File;

public class Main {
    static {
        String osName = System.getProperty("os.name").toLowerCase();
        String libPath = null;

        // Kiểm tra hệ điều hành và xác định thư viện phù hợp
        if (osName.contains("win")) {
            // Đường dẫn thư viện trên Windows
            libPath = "lib/opencv/window/opencv_java490.dll";  // Đảm bảo thư viện nằm trong thư mục này
        } else if (osName.contains("nix") || osName.contains("nux")) {
            // Đường dẫn thư viện trên Linux
            libPath = "lib/opencv/linux/libopencv_java490.so";  // Đảm bảo thư viện nằm trong thư mục này
        }

        // Kiểm tra nếu thư viện tồn tại
        if (libPath != null) {
            File libFile = new File(libPath);
            if (libFile.exists() && libFile.isFile()) {
                // Load thư viện nếu tồn tại
                System.load(libFile.getAbsolutePath());
                System.out.println("Library loaded from: " + libFile.getAbsolutePath());
            } else {
                // Thư viện không tìm thấy
                System.err.println("Library not found at: " + libFile.getAbsolutePath());
            }
        } else {
            System.err.println("Unsupported OS: " + osName);
        }
    }

    public static void main(String[] args) {
        System.out.println("OpenCV version: " + org.opencv.core.Core.VERSION);
    }
}
