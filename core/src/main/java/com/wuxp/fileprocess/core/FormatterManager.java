package com.wuxp.fileprocess.core;

import org.springframework.format.Formatter;

/**
 * formatter manager
 *
 * @author wuxp
 */
public interface FormatterManager {


    /**
     * 添加一个formatter
     *
     * @param formatter
     */
    FormatterManager addFormatter(Formatter formatter);

    /**
     * 设置formatter
     *
     * @param index
     * @param formatter
     */
    FormatterManager setFormatter(int index, Formatter formatter);

    /**
     * 设置formatter
     *
     * @param index   按照java bean的字段名称
     * @param formatter
     */
    FormatterManager setFormatter(String index, Formatter formatter);

    /**
     * 设置formatter
     *
     * @param clazz     按照类型
     * @param formatter
     * @return
     */
    FormatterManager setFormatter(Class<?> clazz, Formatter formatter);

}