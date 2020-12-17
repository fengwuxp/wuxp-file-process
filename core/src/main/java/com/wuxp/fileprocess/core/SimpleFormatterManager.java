package com.wuxp.fileprocess.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;
import org.springframework.format.datetime.DateFormatter;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author wuxp
 */
@Slf4j
public class SimpleFormatterManager implements FormatterManager {

    protected Map<Integer, Formatter> formatters = new LinkedHashMap<>(12);

    protected Map<String, Formatter> formatterMap = new LinkedHashMap<>(12);

    protected static final Map<Class<?>, Object> CLASS_FORMATTER_LINKED_HASH_MAP = new ConcurrentHashMap<>(16);


    /**
     * 默认内置的DateFormatter
     */
    public static final DateFormatter DATE_FORMATTER = new DateFormatter("yyyy年MM月dd日HH时mm分ss秒");

    static {
        CLASS_FORMATTER_LINKED_HASH_MAP.put(Date.class, DATE_FORMATTER);
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
        CLASS_FORMATTER_LINKED_HASH_MAP.put(clazz, formatter);
        return this;
    }


    /**
     * 提供给一个静态方法支持全局设置
     *
     * @param clazz
     * @param formatter formatter 需要是线程安全的
     */
    public static void addFormatterByType(Class<?> clazz, Formatter formatter) {
        CLASS_FORMATTER_LINKED_HASH_MAP.put(clazz, formatter);
    }

    /**
     * 提供给一个静态方法支持全局设置
     *
     * @param clazz
     * @param supplier 保证线程安全或者每次返回一个新对象
     */
    public static void addFormatterByType(Class<?> clazz, Supplier<Formatter> supplier) {
        CLASS_FORMATTER_LINKED_HASH_MAP.put(clazz, supplier);
    }

    /**
     * 获取 formatter
     * 优先使用 index，其次是字段名称，最后按照类型匹配
     *
     * @param clazz 类类型
     * @param name  字段名称
     * @param index 索引
     * @return
     */
    protected Formatter getFormatter(Class<?> clazz, String name, Integer index) {
        Formatter formatter = this.formatters.get(index);
        if (formatter != null) {
            return formatter;
        }
        formatter = this.formatterMap.get(name);
        if (formatter != null) {
            return formatter;
        }
        Object supplier = CLASS_FORMATTER_LINKED_HASH_MAP.get(clazz);
        if (supplier instanceof Supplier) {
            return ((Supplier<Formatter>) supplier).get();
        }
        return (Formatter) supplier;
    }

    ;
}
