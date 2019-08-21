package com.wuxp.fileprocess.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * simple file processing task manager
 */
@Slf4j
public class SimpleFileProcessingTaskManager implements FileProcessingTaskManager {

    private final static Map<String, FileProcessingTask> FILE_PROCESSING_TASK_MAP = new ConcurrentReferenceHashMap<>();

    @Autowired(required = false)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public String join(FileProcessingTask processingTask) {

        if (!FILE_PROCESSING_TASK_MAP.containsKey(processingTask.getProcessIdentifies())) {

            FileProcessingTask fileProcessingTask = FILE_PROCESSING_TASK_MAP.get(processingTask.getProcessIdentifies());
            return fileProcessingTask.getProcessIdentifies();
        }
        threadPoolTaskExecutor.execute(processingTask);

        return processingTask.getProcessIdentifies();
    }

    @Override
    public FileProcessingTask get(String identifies) {
        return FILE_PROCESSING_TASK_MAP.get(identifies);
    }

    @Override
    public FileProcessingTask remove(String identifies) {
        return FILE_PROCESSING_TASK_MAP.remove(identifies);
    }
}
