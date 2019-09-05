package com.wuxp.fileprocess.excel;


import com.wuxp.fileprocess.core.FileProcessingTask;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Map;

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
     *
     * @return
     */
    int getCurrentSheetIndex();

    /**
     * 获取当前sheet 的总条数
     *
     * @return
     */
    int getCurrentSheetTotal();


    /**
     * 设置单列的样式
     *
     * @param index
     * @param cellStyle
     * @return
     */
    ExcelFileProcessingTask put(int index, CellStyle cellStyle);

    /**
     * 设置所有列的样式
     *
     * @param cellStyles
     */
    void setCellStyles(Map<Integer, CellStyle> cellStyles);

}
