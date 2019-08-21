package com.wuxp.fileprocess.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleFormatterManager implements FormatterManager {

    protected List<Formatter> formatters = new ArrayList<>();

    protected Map<String, Formatter> formatterMap = new LinkedHashMap<>();


    @Override
    public FormatterManager addFormatter(Formatter formatter) {
        formatters.add(formatter);
        return this;
    }

    @Override
    public FormatterManager setFormatter(int index, Formatter formatter) {
        formatters.set(index, formatter);
        return this;
    }

    @Override
    public FormatterManager setFormatter(String index, Formatter formatter) {
        this.formatterMap.put(index, formatter);
        return this;
    }
}
