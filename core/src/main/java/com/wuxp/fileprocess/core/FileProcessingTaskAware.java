package com.wuxp.fileprocess.core;

/**
 * 任务的前后处理
 */
public interface FileProcessingTaskAware {


    /**
     * 任务的前置处理
     *
     * @param fileProcessingTask
     */
    default void preProcess(FileProcessingTask fileProcessingTask) {

    }


    /**
     * 每处理一条记录的回调
     *
     * @param fileProcessingTask
     * @param rowData
     */
    default void process(FileProcessingTask fileProcessingTask, Object rowData) {

    }

    /**
     * 任务的后置处理
     *
     * @param fileProcessingTask
     * @param fileProcessingTaskManager
     */
    default void postProcess(FileProcessingTask fileProcessingTask, FileProcessingTaskManager fileProcessingTaskManager) {

    }
}
