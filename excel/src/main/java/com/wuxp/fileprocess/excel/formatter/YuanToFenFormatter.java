package com.wuxp.fileprocess.excel.formatter;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author wuxp
 * <p>
 * 元转分
 * Created by wuxp on 2017/7/13.
 */
public class YuanToFenFormatter implements Formatter<Number> {

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        return new BigDecimal(text).multiply(new BigDecimal(100)).longValue();
    }

    @Override
    public String print(Number object, Locale locale) {
        if (object == null) {
            return null;
        }
        return new BigDecimal(object.toString()).multiply(new BigDecimal(100)).toBigInteger().toString();
    }
}
