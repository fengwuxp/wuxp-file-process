package com.wuxp.fileprocess.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.Formatter;

import java.util.Map;


/**
 * 导出 excel 每一个cell的描述
 *
 * @author wuxp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportExcelCell {


    /**
     * 取值表达式
     * 默认使用spel 表达式
     * {@link org.springframework.expression.spel.standard.SpelExpressionParser}
     */
    private String value;


    /**
     * 当前列的标题
     */
    private String title;


    /**
     * 格式化数值的达式，仅在当前列对应的数据类型为数值类型是使用
     *
     * @see org.springframework.format.number.NumberStyleFormatter
     */
    private String numStylePattern;

    /**
     * map装换数据源，在该对象不为空时，将自动启用map转换器
     *
     * @key 属性名称
     * @value 需要转换的值
     */
    private Map<String, String> mapFormatterSource;

    /**
     * 转换器
     */
    private Formatter formatter;

    /**
     * 列宽
     */
    private Integer width;


}
