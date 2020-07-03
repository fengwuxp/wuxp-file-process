package com.wuxp.fileprocess.excel;


import com.wuxp.fileprocess.excel.model.ExcelRowDataHandleResult;

import java.io.OutputStream;
import java.util.List;

/**
 * excel import processing task
 *
 * @author wuxp
 */
public interface ImportExcelFileProcessingTask extends ExcelFileProcessingTask {

    /**
     * 获取失败的结果列表
     * 返回被 {@link ImportExcelRowDataConverter} 转换过的数据列表
     *
     * @param <T>
     * @return
     */
    <T> List<T> getFailureList();

    /**
     * 返回处理失败的行记录
     *
     * @return
     */
    List<String[]> getFailureRows();


    /**
     * 导出失败文件
     *
     * @param outputStream
     */
    void exportFailureFile(OutputStream outputStream);


    /**
     * 导入数据的 row 数据转换
     *
     * @param <T>
     */
    @FunctionalInterface()
    interface ImportExcelRowDataConverter<T> {

        /**
         * 转换数据
         *
         * @param row current row data
         * @return java bean
         */
        T convert(List<String> row);

    }

    /**
     * 导入数据的处理器
     *
     * @param <T>
     */
    @FunctionalInterface()
    interface ImportExcelRowDataHandler<T> {

        /**
         * 处理导入数据
         *
         * @param data
         * @return
         */
        ExcelRowDataHandleResult handle(T data);
    }


}
