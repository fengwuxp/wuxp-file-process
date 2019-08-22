package com.wuxp.fileprocess.core;

import org.springframework.format.Formatter;

/**
 * formatter manager
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
     * @param index
     * @param formatter
     */
    FormatterManager setFormatter(String index, Formatter formatter);

    /**
     * 设置formatter
     *
     * @param clazz
     * @param formatter
     * @return
     */
    FormatterManager setFormatter(Class<?> clazz, Formatter formatter);

}