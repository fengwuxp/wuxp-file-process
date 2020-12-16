package com.wuxp.fileprocess.config;


import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.core.SimpleFileProcessingTaskManager;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.wuxp.fileprocess.core.FileProcessingTaskManager.FILE_PROCESS_TASK_EXECUTOR_BEAN_NAME;


/**
 * file processing task auto configuration
 *
 * @author wuxp
 */
@Configuration
@EnableConfigurationProperties(FileProcessingTaskProperties.class)
@ConditionalOnProperty(prefix = FileProcessingTaskProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class FileProcessingTaskAutoConfiguration {

    private static final String EXECUTOR_THREAD_NAME = "file_process_";

    private static final AtomicLong THREADER_COUNTER = new AtomicLong(0);

    @Bean(name = {FILE_PROCESS_TASK_EXECUTOR_BEAN_NAME}, destroyMethod = "shutdown")
    @ConditionalOnMissingBean(name = {FILE_PROCESS_TASK_EXECUTOR_BEAN_NAME})
    public ThreadPoolExecutor threadPoolTaskExecutor() {
        return new ThreadPoolExecutor(1, 4, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(32), runnable -> {
            SecurityManager securityManager = System.getSecurityManager();
            long count = THREADER_COUNTER.incrementAndGet();
            ThreadGroup group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            Thread thread = new Thread(group, runnable, EXECUTOR_THREAD_NAME + count, 0);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }, new ThreadPoolExecutor.CallerRunsPolicy());
    }


    @Bean
    @ConditionalOnMissingBean(FileProcessingTaskManager.class)
    @ConditionalOnBean(name = {FILE_PROCESS_TASK_EXECUTOR_BEAN_NAME})
    public FileProcessingTaskManager fileProcessingTaskManager() {
        return new SimpleFileProcessingTaskManager();
    }
}
