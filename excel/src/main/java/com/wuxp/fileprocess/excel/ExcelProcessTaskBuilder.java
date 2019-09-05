package com.wuxp.fileprocess.excel;

/**
 * excel file process task builder
 */
public interface ExcelProcessTaskBuilder {

    String start(ExcelProcessTaskFactory factory);

    String start();

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
