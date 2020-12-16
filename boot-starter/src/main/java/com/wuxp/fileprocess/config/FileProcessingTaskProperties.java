package com.wuxp.fileprocess.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuxp
 */
@Data
@ConfigurationProperties(prefix = FileProcessingTaskProperties.PREFIX)
public class FileProcessingTaskProperties {

    public static final String PREFIX = "spring.file";


    /**
     * 是否启用
     */
    private Boolean enabled = true;


}
