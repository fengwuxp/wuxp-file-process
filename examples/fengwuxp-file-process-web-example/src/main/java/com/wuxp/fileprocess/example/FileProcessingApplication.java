package com.wuxp.fileprocess.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.wuxp"})
public class FileProcessingApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileProcessingApplication.class, args);
    }
}
