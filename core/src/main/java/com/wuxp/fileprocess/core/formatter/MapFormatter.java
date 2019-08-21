package com.wuxp.fileprocess.core.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

/**
 * @author wuxp
 * <p>
 * 根据map进行值转换
 * Created by wuxp on 2017/7/15.
 */
@Slf4j
public class MapFormatter implements Formatter {

    private Map<String, String> dataSource;

    public MapFormatter(Map<String, String> map) {
        this.dataSource = map;
    }

    @Override
    public Object parse(String s, Locale locale) throws ParseException {
        for (Map.Entry<String, String> entry : this.dataSource.entrySet()) {
            if (entry.getValue().equals(s)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public String print(Object o, Locale locale) {
        if (o == null) {
            return "";
        }
        return this.dataSource.get(o.toString());
    }
}
