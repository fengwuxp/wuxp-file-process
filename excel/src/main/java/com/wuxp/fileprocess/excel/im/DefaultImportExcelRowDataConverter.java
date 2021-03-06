package com.wuxp.fileprocess.excel.im;

import com.wuxp.fileprocess.core.SimpleFormatterManager;
import com.wuxp.fileprocess.excel.ImportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.formatter.ExportCellDataFormatter;
import com.wuxp.fileprocess.excel.utils.SimpleDateFormatExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 默认的 row data converter
 *
 * @param <T>
 * @author wuxp
 */
@Slf4j
public class DefaultImportExcelRowDataConverter<T> extends SimpleFormatterManager implements ImportExcelFileProcessingTask.ImportExcelRowDataConverter<T> {

    private final Map<Integer, String> filedNameIndexMap = new LinkedHashMap<>();

    private final Class<T> tClass;

    public DefaultImportExcelRowDataConverter(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public T convert(List<String> row) {

        if (row == null) {
            return null;
        }
        int size = row.size();
        Map<String, Object> resultData = new HashMap<String, Object>(size);

        filedNameIndexMap.forEach((index, val) -> {
            //属性名称
            String filedName = filedNameIndexMap.get(index);
            if (!StringUtils.hasText(filedName)) {
                throw new RuntimeException("未获取到file name,在第" + index + "列数据中");
            }
            if (index > size - 1) {
                if (log.isDebugEnabled()) {
                    log.debug("数据列数：{},index={}", size, index);
                }
                resultData.put(filedName, null);
                return;
            }
            String cellData = row.get(index);
            Formatter formatter = getFormatter(val.getClass(), filedName, index);
            if (formatter == null) {
                resultData.put(filedName, cellData);
                return;
            }
            if (formatter instanceof ExportCellDataFormatter) {
                try {
                    Object result = ((ExportCellDataFormatter) formatter).parse(cellData, row);
                    resultData.put(filedName, result);
                } catch (ParseException e) {
                    if (log.isInfoEnabled()) {
                        log.info("parse value error, filedName={},message={}", filedName, e.getMessage(), e);
                    }
                }
            } else {
                try {
                    Object result = formatter.parse(cellData, Locale.CHINA);
                    resultData.put(filedName, result);
                } catch (ParseException e) {
                    if (log.isInfoEnabled()) {
                        log.info("parse value error, filedName={},message={}", filedName, e.getMessage(), e);
                    }
                }
            }
        });

        T target = null;
        try {
            target = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            if (log.isInfoEnabled()) {
                log.info("反射实例化导入对象失败，message={}", e.getMessage(), e);
            }
        }

        //使用spring 的 dataBinder
        DataBinder dataBinder = new DataBinder(target);
        //注册时间对象的处理
        DateFormat dateFormat = new SimpleDateFormatExt();
        dateFormat.setLenient(true);
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        dataBinder.bind(new MutablePropertyValues(resultData));

        return target;

    }

    /**
     * 设置 字段名称和excel cell index关系
     *
     * @param cellIndex
     * @param filedName
     * @return
     */
    public DefaultImportExcelRowDataConverter<T> putFiledName(int cellIndex, String filedName) {
        this.filedNameIndexMap.put(cellIndex, filedName);
        return this;
    }
}
