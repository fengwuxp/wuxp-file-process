package com.wuxp.fileprocess.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * simple file processing task manager
 *
 * @author wuxp
 */
@Slf4j
public class SimpleFileProcessingTaskManager implements FileProcessingTaskManager, InitializingBean {

    /**
     * 任务处理缓存
     */
    private final static Map<String, FileProcessingTask> FILE_PROCESSING_TASK_MAP = new ConcurrentReferenceHashMap<>(32);

    @Resource(name = FILE_PROCESS_TASK_EXECUTOR_BEAN_NAME)
    private Executor executor;


    @Override
    public String join(FileProcessingTask processingTask) {

        String processIdentifies = processingTask.getProcessIdentifies();
        if (FILE_PROCESSING_TASK_MAP.containsKey(processIdentifies)) {

            FileProcessingTask fileProcessingTask = FILE_PROCESSING_TASK_MAP.get(processIdentifies);
            return fileProcessingTask.getProcessIdentifies();
        }
        executor.execute(processingTask);
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

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(executor, "文件处理的Executor不能为null");
    }
}
