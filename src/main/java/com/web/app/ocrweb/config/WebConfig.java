package com.web.app.ocrweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình phục vụ các tệp log từ thư mục logs
        registry.addResourceHandler("/logs/**")
                .addResourceLocations("file:logs/"); // Đảm bảo rằng thư mục logs đã tồn tại và có quyền truy cập
    }
}
