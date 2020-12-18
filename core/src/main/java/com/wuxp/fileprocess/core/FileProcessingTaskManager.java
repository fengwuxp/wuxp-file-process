package com.wuxp.fileprocess.core;

import java.util.concurrent.Executor;

/**
 * 文件处理任务管理器
 *
 * @author wuxp
 */
public interface FileProcessingTaskManager {

    /**
     * 文件处理的 {@link Executor} bean name
     */
    String FILE_PROCESS_TASK_EXECUTOR_BEAN_NAME = "file_processing_task_executor";


    /**
     * 加入任务
     *
     * @param processingTask 处理任务
     * @return 返回任务唯一标识
     */
    String join(FileProcessingTask processingTask);


    /**
     * 获取任务
     *
     * @param identifies 任务标识
     * @return
     */
    FileProcessingTask get(String identifies);


    /**
     * 移除任务，如果任务未结束，将不会进行移除
     *
     * @param identifies
     * @return
     */
    FileProcessingTask remove(String identifies);

    /**
     * 关闭并移除任务
     *
     * @param identifies
     * @return
     */
    FileProcessingTask closeAndRemove(String identifies);


}
