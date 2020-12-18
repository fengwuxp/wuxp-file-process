package com.wuxp.fileprocess.excel;

import com.wuxp.fileprocess.core.FileProcessingTaskManager;

/**
 * excel file process task builder
 *
 * @author wuxp
 */
public interface ExcelProcessTaskBuilder {

    /**
     * 启动任务
     *
     * @param factory 处理任务工厂
     * @return 任务id
     */
    String start(ExcelProcessTaskFactory factory);

    /**
     * 启动任务
     *
     * @return 任务id
     */
    String start();

    /**
     * 获取任务管理器
     *
     * @return
     */
    FileProcessingTaskManager getFileProcessingTaskManager();

    /**
     * task factory
     *
     * @param <B>
     */
    @FunctionalInterface
    interface ExcelProcessTaskFactory<B extends ExcelProcessTaskBuilder> {

        ExcelFileProcessingTask factory(B builder);
    }
}
