package com.wuxp.fileprocess.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxp
 */
@SpringBootApplication(scanBasePackages = {"com.wuxp"})
public class FileProcessingApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileProcessingApplication.class, args);
    }

    @Bean
    public ThreadPoolExecutor threadPoolTaskExecutor() {
        return new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(64));
    }
}
