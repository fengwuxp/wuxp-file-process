package com.wuxp.fileprocess.example.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = FileProcessingTaskProperties.PREFIX)
public class FileProcessingTaskProperties {

    public static final String PREFIX = "spring.file";


    /**
     * 是否启用
     */
    private Boolean enabled = true;


    /**
     * 任务处理线程池相关配置
     */
    private TaskPoolProperties taskPoolProperties = new TaskPoolProperties();

}
