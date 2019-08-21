package com.wuxp.fileprocess.example.excel;


import com.wuxp.fileprocess.core.FileProcessingTask;

/**
 * excel file processing task
 */
public interface ExcelFileProcessingTask extends FileProcessingTask {


    /**
     * 获取处理的总条数
     *
     * @return
     */
    int getProcessTotal();

    /**
     * 获取处理成功的条数
     *
     * @return
     */
    int getSuccessTotal();

    /**
     * 获取处理失败的条数
     *
     * @return
     */
    int getFailureTotal();

    /**
     * 获取 sheet 的总数
     *
     * @return
     */
    int getSheetTotal();

    /**
     * 获取当前 current index
     * @return
     */
    int getCurrentSheetIndex();

    /**
     * 获取当前sheet 的总条数
     *
     * @return
     */
    int getCurrentSheetTotal();


}
