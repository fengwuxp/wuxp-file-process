package com.wuxp.fileprocess.excel;

import com.wuxp.fileprocess.core.FileProcessingTaskManager;

/**
 * excel file process task builder
 */
public interface ExcelProcessTaskBuilder {

    String start(ExcelProcessTaskFactory factory);

    String start();

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
