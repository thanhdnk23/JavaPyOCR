package com.web.app.ocrweb.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);     // Số lượng luồng chính trong pool
        executor.setMaxPoolSize(5);      // Số lượng luồng tối đa trong pool
        executor.setQueueCapacity(100);  // Kích thước hàng đợi
        executor.setThreadNamePrefix("OCRWorker-");
        executor.initialize();
        return executor;
    }
}
