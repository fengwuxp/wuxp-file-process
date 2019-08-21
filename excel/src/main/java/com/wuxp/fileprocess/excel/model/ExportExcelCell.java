package com.wuxp.fileprocess.excel.model;

import lombok.Data;
import org.springframework.format.Formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 导出 excel 每一个cell的描述
 */
@Data
public class ExportExcelCell {


    /**
     * 取值表达式
     */
    private String value;


    /**
     * 当前列的标题
     */
    private String title;


    /**
     * 格式化表正则达式
     */
    private String formatterPattern;

    /**
     * map装换数据源，在该对象不为空时，将自动启用map转换器
     */
    private Map<String/*属性名称*/, Object> mapFormatterSource;

    /**
     * 转换器
     */
    private List<Formatter> formatter = new ArrayList<Formatter>();

    /**
     * 列宽
     */
    private Integer width;


}
