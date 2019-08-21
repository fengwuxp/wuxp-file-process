package com.wuxp.fileprocess.core;

/**
 * 文件处理任务管理器
 */
public interface FileProcessingTaskManager {


    /**
     * 加入任务
     *
     * @param processingTask
     * @return 返回任务唯一标识
     */
    String join(FileProcessingTask processingTask);


    /**
     * 获取任务
     *
     * @param identifies
     * @return
     */
    FileProcessingTask get(String identifies);


    /**
     * 移除任务
     *
     * @param identifies
     * @return
     */
    FileProcessingTask remove(String identifies);

}
