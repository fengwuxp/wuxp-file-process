package com.wuxp.fileprocess.excel.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

/**
 * @author wuxp
 *
 * 根据map进行值转换
 * Created by wuxp on 2017/7/15.
 */
public class MapFormatter implements Formatter {

    private Map<String, String> map;

    public MapFormatter(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public Object parse(String s, Locale locale) throws ParseException {
        for (Map.Entry<String, String> entry : this.map.entrySet()) {
            if (entry.getValue().equals(s)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public String print(Object o, Locale locale) {
        if (o==null){
            return "";
        }
        return this.map.get(o.toString());
    }
}
