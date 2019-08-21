package com.wuxp.fileprocess.example.excel.formatter;


import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;


/**
 * excel cell data formatter
 *
 * @param <T> parse result type
 * @param <E> row data item type
 */
public interface ExportCellDataFormatter<T, E> extends Formatter<T> {


    @Override
    default T parse(String text, Locale locale) throws ParseException {

        return null;
    }

    @Override
    default String print(T object, Locale locale) {
        return null;
    }

    /**
     * 处理每一列的值
     *
     * @param cellValue 当前列的值
     * @param rowData   当前行的值
     * @return T
     * @throws ParseException
     */
    T parse(String cellValue, List<E> rowData) throws ParseException;
}
