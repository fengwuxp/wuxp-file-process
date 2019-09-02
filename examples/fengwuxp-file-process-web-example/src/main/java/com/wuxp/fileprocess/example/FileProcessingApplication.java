package com.wuxp.fileprocess.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.wuxp"})
@EnableScheduling
public class FileProcessingApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileProcessingApplication.class, args);
    }
}
