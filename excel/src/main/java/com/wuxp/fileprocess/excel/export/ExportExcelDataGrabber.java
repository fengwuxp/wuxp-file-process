package com.wuxp.fileprocess.excel.export;

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
     * 默认可以返回总条数，直到查询到没有数据为止
     *
     * @return
     */
    default long getTotalNumber() {
        return -1L;
    }

    /**
     * 断言是否需要拒绝本次任务
     *
     * @param total 查询总数
     * @throws RuntimeException 如果拒绝则抛出异常
     */
    default void assertReject(long total) throws RuntimeException {

    }


}
