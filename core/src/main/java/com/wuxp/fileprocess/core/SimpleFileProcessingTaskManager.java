package com.wuxp.fileprocess.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * simple file processing task manager
 */
@Slf4j
public class SimpleFileProcessingTaskManager implements FileProcessingTaskManager {

    // 任务处理缓存
    private final static Map<String, FileProcessingTask> FILE_PROCESSING_TASK_MAP = new ConcurrentReferenceHashMap<>();

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public String join(FileProcessingTask processingTask) {

        String processIdentifies = processingTask.getProcessIdentifies();
        if (FILE_PROCESSING_TASK_MAP.containsKey(processIdentifies)) {

            FileProcessingTask fileProcessingTask = FILE_PROCESSING_TASK_MAP.get(processIdentifies);
            return fileProcessingTask.getProcessIdentifies();
        }
        threadPoolTaskScheduler.execute(processingTask);

        FILE_PROCESSING_TASK_MAP.put(processIdentifies, processingTask);

        return processIdentifies;
    }

    @Override
    public FileProcessingTask get(String identifies) {
        return FILE_PROCESSING_TASK_MAP.get(identifies);
    }

    @Override
    public FileProcessingTask remove(String identifies) {
        FileProcessingTask fileProcessingTask = this.get(identifies);
        if (fileProcessingTask == null) {
            return null;
        }
        if (!fileProcessingTask.isEnd()) {
            return fileProcessingTask;
        }
        return FILE_PROCESSING_TASK_MAP.remove(identifies);
    }

    @Override
    public FileProcessingTask closeAndRemove(String identifies) {

        FileProcessingTask fileProcessingTask = this.get(identifies);
        if (fileProcessingTask == null) {
            return null;
        }
        if (fileProcessingTask.isEnd()) {
            return FILE_PROCESSING_TASK_MAP.remove(identifies);
        }
        //中断任务
        fileProcessingTask.interruptProcess();

        return FILE_PROCESSING_TASK_MAP.remove(identifies);
    }
}
