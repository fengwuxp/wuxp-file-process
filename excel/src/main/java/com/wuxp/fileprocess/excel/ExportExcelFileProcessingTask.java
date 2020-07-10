package com.wuxp.fileprocess.excel;


import com.alibaba.excel.metadata.Sheet;

import java.io.OutputStream;
import java.util.List;

/**
 * excel export file process task
 * @author wuxp
 */
public interface ExportExcelFileProcessingTask extends ExcelFileProcessingTask {


    /**
     * 导出文件
     *
     * @param outputStream
     */
    void exportFile(OutputStream outputStream);


    /**
     * 创建 sheet
     *
     * @param index sheet的 index
     * @return
     */
    Sheet createNewSheet(int index);

    /**
     * 导出数据的 行数据转换
     */
    @FunctionalInterface
    interface ExportExcelRowDataConverter<T> {

        /**
         * 将 java bean数据转换为 row data
         *
         * @param data
         * @return
         */
        List<String> converter(T data);
    }

}
