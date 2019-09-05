package com.wuxp.fileprocess.excel.export;

import com.wuxp.fileprocess.core.SimpleFormatterManager;
import com.wuxp.fileprocess.excel.ExportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.formatter.ExportCellDataFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.format.Formatter;
import org.springframework.format.datetime.DateFormatter;

import java.util.*;

/**
 * default export excel row data converter
 * <p>
 * {@link org.springframework.expression.spel.standard.SpelExpressionParser}
 */
@Slf4j
public class DefaultExportExcelRowDataConverter<T> extends SimpleFormatterManager implements ExportExcelFileProcessingTask.ExportExcelRowDataConverter<T> {


    private Map<Integer, String> filedNameMapIndex = new LinkedHashMap<>();

    /**
     * 表达式解析器
     */
    private ExpressionParser expressionParser = new SpelExpressionParser();

    /**
     * 取值表达式
     */
    private List<String> valueExpressions;

    /**
     * 用来缓存的表达式的临时结果
     */
    private Map<String/*表达式*/, Expression> expressionMap = new HashMap<>();

    public DefaultExportExcelRowDataConverter(List<String> valueExpressions) {
        this.valueExpressions = valueExpressions;
    }


    @Override
    public List<String> converter(T data) {
        if (data == null) {
            return null;
        }

        List<String> result = new ArrayList<>();

        int size = valueExpressions.size();
        for (int i = 0; i < size; i++) {
            if (i > size - 1) {
                log.warn("数据列数：{},index={}", size, i);
                result.add(null);
                break;
            }
            String valueExpression = valueExpressions.get(i);
            Expression expression = this.expressionMap.get(valueExpression);
            if (expression == null) {
                expression = expressionParser.parseExpression(valueExpression);
                this.expressionMap.put(valueExpression, expression);
            }
            Object value = null;
            try {
                value = expression.getValue(data);
            } catch (Exception e) {
                log.warn("name-> " + valueExpression + "的值为找到，异常：" + e.getMessage());
            }

            if (value == null) {
                result.add(null);
            } else {
                result.add(this.formatterValue(value, i, data));
            }
        }


        return result;
    }

    /**
     * 格式化数据
     *
     * @param val
     * @param cellIndex
     * @param data
     * @return
     */
    protected String formatterValue(Object val, int cellIndex, T data) {

        String filedName = filedNameMapIndex.get(cellIndex);

        Formatter formatter = getFormatter(val.getClass(), filedName, cellIndex);
        if (formatter == null) {
            return val.toString();
        }
        String result = null;
        if (formatter instanceof ExportCellDataFormatter) {
            result = ((ExportCellDataFormatter<Object, T>) formatter).print(val, data);
        } else {
            result = formatter.print(val, Locale.CHINA);
        }

        if (result == null) {
            return val.toString();
        }
        return result;

    }
}
