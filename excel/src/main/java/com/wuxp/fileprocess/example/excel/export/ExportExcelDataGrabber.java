package com.wuxp.fileprocess.example.excel.export;

import java.util.List;


/**
 * 抓取 导出excel 的数据
 *
 * @param <T>
 */
public interface ExportExcelDataGrabber<T> {

    /**
     * 抓取数据
     *
     * @param page      开始抓取数据的页码 从0开始
     * @param fetchSize 抓取大小
     * @return
     */
    List<T> fetchData(int page, int fetchSize);

    /**
     * 获取导出的总条数
     *
     * @return
     */
    long getTotalNumber();


}
