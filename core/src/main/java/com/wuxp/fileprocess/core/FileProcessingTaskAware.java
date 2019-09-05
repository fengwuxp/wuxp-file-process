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
     * 任务的后置处理
     *
     * @param fileProcessingTask
     */
    default void postProcess(FileProcessingTask fileProcessingTask) {

    }
}
