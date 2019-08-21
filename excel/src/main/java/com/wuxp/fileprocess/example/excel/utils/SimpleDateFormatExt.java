package com.wuxp.fileprocess.example.excel.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wuxp
 *
 * 支持匹配转换多种格式的时间数据
 * Created by wuxp on 2017/6/28.
 */
public class SimpleDateFormatExt extends SimpleDateFormat {


    public SimpleDateFormatExt() {
    }

    @Override
    public Date parse(String source) throws ParseException {
        if (!StringUtils.hasText(source)) {
            return null;
        }

        switch (source.length()){
            case 7:
                return parseDate(source, "yyyy-MM");
            case 10:
                return parseDate(source, "yyyy-MM-dd");
            case 16:
                return parseDate(source, "yyyy-MM-dd hh:mm");
            case 19:
                return parseDate(source, "yyyy-MM-dd hh:mm:ss");
            default:
                throw new ParseException("Invalid  value '" + source + "'",-1);
        }
//        if (source.matches("^\\d{4}-\\d{1,2}$")) {
//            return parseDate(source, 0);
//        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
//            return parseDate(source, 1);
//        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
//            return parseDate(source, 2);
//        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
//            return parseDate(source, 3);
//        } else {
//            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
//        }
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期
     * @param pattern   使用第几个时间转换器
     * @return Date 日期
     */
    private Date parseDate(String dateStr, String pattern) throws ParseException {
        return  new SimpleDateFormat(pattern).parse(dateStr);
    }
}
