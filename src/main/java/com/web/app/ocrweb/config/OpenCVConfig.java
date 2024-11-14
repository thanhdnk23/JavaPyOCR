package com.web.app.ocrweb.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCVConfig {

    // Biến tĩnh để kiểm tra trạng thái đã tải thư viện hay chưa
    private static boolean isLibraryLoaded = false;

    @Bean
    public String opencvLibraryPath() {
        // Nếu thư viện đã được tải rồi, không tải lại
        if (isLibraryLoaded) {
            System.out.println("Library is already loaded.");
            return "";  // Trả về một chuỗi trống nếu không cần tải lại thư viện
        }

        // Lấy cấu hình hệ điều hành từ enum
        OpenCVConfigEnum config = OpenCVConfigEnum.getConfigForCurrentOS();
        String libPath = config.getLibraryPath();

        File libFile = new File(libPath);
        if (libFile.exists() && libFile.isFile()) {
            try {
                // Tải thư viện OpenCV chỉ khi chưa được tải
                System.load(libFile.getAbsolutePath());
                System.out.println("Library loaded from: " + libFile.getAbsolutePath());
                isLibraryLoaded = true;  // Đánh dấu thư viện đã được tải
            } catch (UnsatisfiedLinkError e) {
                System.err.println("Error loading OpenCV library: " + e.getMessage());
            }
        } else {
            System.err.println("Library not found at: " + libFile.getAbsolutePath());
        }
 
        return libPath;  // Trả về đường dẫn thư viện để có thể sử dụng ở nơi khác nếu cần
    }
}
