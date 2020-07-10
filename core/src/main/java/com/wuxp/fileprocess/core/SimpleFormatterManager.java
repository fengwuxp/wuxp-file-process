package com.wuxp.fileprocess.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;
import org.springframework.format.datetime.DateFormatter;

import java.util.*;

/**
 * @author wuxp
 */
@Slf4j
public class SimpleFormatterManager implements FormatterManager {

    protected Map<Integer, Formatter> formatters = new LinkedHashMap<>();

    protected Map<String, Formatter> formatterMap = new LinkedHashMap<>();

    protected Map<Class<?>, Formatter> classFormatterMap = new LinkedHashMap<>();


    /**
     * 默认内置的DateFormatter
     */
    public static final DateFormatter DATE_FORMATTER = new DateFormatter("yyyy年MM月dd日HH时mm分ss秒");

    {
        this.classFormatterMap.put(Date.class, DATE_FORMATTER);
    }


    @Override
    public FormatterManager addFormatter(Formatter formatter) {
        int size = this.formatters.size();
        formatters.put(size, formatter);
        return this;
    }

    @Override
    public FormatterManager setFormatter(int index, Formatter formatter) {
        formatters.put(index, formatter);
        return this;
    }

    @Override
    public FormatterManager setFormatter(String index, Formatter formatter) {
        this.formatterMap.put(index, formatter);
        return this;
    }

    @Override
    public FormatterManager setFormatter(Class<?> clazz, Formatter formatter) {
        this.classFormatterMap.put(clazz, formatter);
        return this;
    }

    /**
     * 获取 formatter
     *
     * @param clazz
     * @param name
     * @param index
     * @return
     */
    protected Formatter getFormatter(Class<?> clazz, String name, Integer index) {

        Formatter formatter = this.classFormatterMap.get(clazz);
        if (formatter != null) {
            return formatter;
        }
        formatter = this.formatterMap.get(name);
        if (formatter != null) {
            return formatter;
        }
        return this.formatters.get(index);
    }

    ;
}
