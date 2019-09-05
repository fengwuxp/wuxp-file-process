package com.wuxp.fileprocess.excel.im;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.wuxp.fileprocess.excel.AbstractExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.ImportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.model.ExcelRowDataHandleResult;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * default import excel file processing task
 */
@Slf4j
public class DefaultImportExcelFileProcessingTask extends AbstractExcelFileProcessingTask implements ImportExcelFileProcessingTask {

    /**
     * 处理的文件
     */
    protected InputStream inputStream;

    /**
     * sheets
     */
    protected List<Sheet> sheets;

    /**
     * 处理失败的条数
     */
    protected List<String[]> failureRows = new ArrayList<>();

    /**
     * 数据converter
     */
    protected ImportExcelRowDateConverter importExcelRowDateConverter;

    /**
     * 导入处理器
     */
    protected ImportExcelRowDataHandler importExcelRowDataHandler;


    /**
     * 表头的行数
     */
    private Integer headTitleLine = 1;


    /**
     * @param file
     * @param importExcelRowDateConverter
     * @param importExcelRowDataHandler
     */
    public DefaultImportExcelFileProcessingTask(File file,
                                                int headTitleLine,
                                                ImportExcelRowDateConverter importExcelRowDateConverter,
                                                ImportExcelRowDataHandler importExcelRowDataHandler) throws FileNotFoundException {
        this(file.getName(), headTitleLine, new FileInputStream(file), importExcelRowDateConverter, importExcelRowDataHandler);
    }

    public DefaultImportExcelFileProcessingTask(String name,
                                                int headTitleLine,
                                                InputStream inputStream,
                                                ImportExcelRowDateConverter importExcelRowDateConverter,
                                                ImportExcelRowDataHandler importExcelRowDataHandler) {
        super(name);
        this.inputStream = inputStream;
        this.importExcelRowDateConverter = importExcelRowDateConverter;
        this.importExcelRowDataHandler = importExcelRowDataHandler;
        this.headTitleLine = headTitleLine;
    }

    @Override
    public <T> List<T> getFailureList() {
        return this.failureRows.stream()
                .map(strings -> (T) this.importExcelRowDateConverter.convert(Arrays.asList(strings)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<String[]> getFailureRows() {
        return this.failureRows;
    }


    @Override
    protected void process() throws Exception {
        log.info("开始导入任务的处理");
        ExcelReader excelReader = EasyExcelFactory.getReader(this.inputStream, new AnalysisEventListener<List<String>>() {
            @Override
            public void invoke(List<String> row, AnalysisContext analysisContext) {
                Sheet currentSheet = analysisContext.getCurrentSheet();
                //跳过表头
                int headLineMun = currentSheet.getHeadLineMun();
                int currentRowNum = analysisContext.getCurrentRowNum();
                if (currentRowNum <= headLineMun) {
                    log.info("跳过表头");
                    return;
                }

                currentSheetTotal = analysisContext.getTotalCount();
                if (currentSheetIndex != currentSheet.getSheetNo()) {
                    //切换了sheet
                    processTotal = processTotal + currentSheet.getSheetNo();
                }
                currentSheetIndex = currentSheet.getSheetNo();
                try {
                    Object data = importExcelRowDateConverter.convert(row);
                    if (data == null) {
                        addFailureRow(row, "构建数据结果为null");
                        return;
                    }
                    ExcelRowDataHandleResult excelRowDataHandleResult = importExcelRowDataHandler.handle(data);
                    if (!excelRowDataHandleResult.isSuccess()) {
                        addFailureRow(row, excelRowDataHandleResult.getFailCause());
                        increaseFailureTotal();
                    } else {
                        increaseSuccessTotal();
                    }
                } catch (Exception e) {
                    log.error("导入处理异常", e);
                    addFailureRow(row, e.getMessage());
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                //处理完成

            }
        });
        List<Sheet> sheets = excelReader.getSheets();
        this.sheets = sheets;
        this.sheetTotal = sheets.size();
        sheets.forEach(excelReader::read);
    }

    @Override
    public void exportFailureFile(OutputStream outputStream) {

        final ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);

        this.sheets.forEach(sheet -> {
//            Sheet failureSheet = new Sheet(1, 1);
            List<List<String>> head = sheet.getHead();
            head.get(0).add("失败原因");
            sheet.setHead(head);
            writer.write0(this.failureRows.stream().map(Arrays::asList).collect(Collectors.toList()), sheet);
        });
        writer.finish();

    }

    /**
     * 添加错误的行记录
     *
     * @param row
     * @param cause
     */
    private void addFailureRow(List<String> row, String cause) {
        List<String> arrayList = new ArrayList<>(row);
        arrayList.add(cause);
        this.failureRows.add(arrayList.toArray(new String[0]));
    }
}
