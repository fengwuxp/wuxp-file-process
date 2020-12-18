package com.wuxp.fileprocess.core.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author wuxp
 * <p>
 * 分转元
 * Created by wuxp on 2017/7/13.
 */
@Slf4j
public class FenToYanFormatter implements Formatter<Number> {

    //public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        return BigDecimal.valueOf(Long.parseLong(text))
                .divide(new BigDecimal(100), 2)
                .setScale(2, BigDecimal.ROUND_UP);
    }

    @Override
    public String print(Number object, Locale locale) {
        if (object == null) {
            return "";
        }
        String s = BigDecimal.valueOf(object.longValue())
                .divide(new BigDecimal(100))
                .setScale(5, BigDecimal.ROUND_UP)
                .toString();
        return s.substring(0, s.indexOf(".") + 3);
    }

}
