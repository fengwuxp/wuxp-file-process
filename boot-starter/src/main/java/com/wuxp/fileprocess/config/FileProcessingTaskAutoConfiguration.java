package com.wuxp.fileprocess.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * file processing task auto configuration
 */
@Configuration
@EnableConfigurationProperties(FileProcessingTaskProperties.class)
@ConditionalOnProperty(prefix = FileProcessingTaskProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class FileProcessingTaskAutoConfiguration {


    @Autowired
    private FileProcessingTaskProperties fileProcessingTaskProperties;


    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskExecutor.class)
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        TaskPoolProperties taskPoolProperties = fileProcessingTaskProperties.getTaskPoolProperties();
        pool.setCorePoolSize(taskPoolProperties.getCorePoolSize());
        pool.setMaxPoolSize(taskPoolProperties.getMaxPoolSize());
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.setKeepAliveSeconds(taskPoolProperties.getKeepAliveSeconds());
        pool.setQueueCapacity(taskPoolProperties.getQueueCapacity());
        return pool;
    }
}
