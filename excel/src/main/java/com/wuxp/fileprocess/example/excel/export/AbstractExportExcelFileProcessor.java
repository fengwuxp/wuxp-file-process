package com.wuxp.fileprocess.example.excel.export;

import com.wuxp.fileprocess.example.excel.ExportExcelFileProcessingTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.format.datetime.DateFormatter;

@Slf4j
public abstract class AbstractExportExcelFileProcessor implements ExportExcelFileProcessingTask {

    /**
     * 默认内置的DateFormatter
     */
    protected static final DateFormatter DATE_FORMATTER = new DateFormatter("yyyy-MM-dd HH:mm:ss");


    /**
     * 表达式解析器
     */
    protected ExpressionParser expressionParser;

    /**
     * 一个工作表导出的最大行数
     */
    protected int sheetMaxRows = 65535;


    /**
     * 一个excel文件支持的最大sheet个数
     */
    protected int maxSheetNum = 10;

    /**
     * 当前工作表序号
     */
    protected int sheetNumber = 0;

    /**
     * 需要导出的总条数
     */
    protected volatile long exportTotal = 0;

    /**
     * 一次抓取数据的大小
     */
    protected int fetchSize = 2000;

}
