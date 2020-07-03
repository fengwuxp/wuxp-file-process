package com.wuxp.fileprocess.excel.im;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.wuxp.fileprocess.core.FileProcessingTask;
import com.wuxp.fileprocess.core.FileProcessingTaskAware;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.AbstractExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.ImportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.model.ExcelRowDataHandleResult;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
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
    protected ImportExcelRowDataConverter importExcelRowDateConverter;

    /**
     * 导入处理器
     */
    protected ImportExcelRowDataHandler importExcelRowDataHandler;


    /**
     * 表头的行数
     */
    private Integer headTitleLine;


    /**
     * @param file
     * @param headTitleLine
     * @param importExcelRowDateConverter
     * @param importExcelRowDataHandler
     * @param fileProcessingTaskAware
     * @param fileProcessingTaskManager
     * @throws FileNotFoundException
     */
    public DefaultImportExcelFileProcessingTask(File file,
                                                int headTitleLine,
                                                ImportExcelRowDataConverter importExcelRowDateConverter,
                                                ImportExcelRowDataHandler importExcelRowDataHandler,
                                                FileProcessingTaskAware fileProcessingTaskAware,
                                                FileProcessingTaskManager fileProcessingTaskManager) throws FileNotFoundException {
        this(file.getName(),
                headTitleLine,
                new FileInputStream(file),
                importExcelRowDateConverter,
                importExcelRowDataHandler,
                fileProcessingTaskAware,
                fileProcessingTaskManager);
    }

    public DefaultImportExcelFileProcessingTask(String name,
                                                int headTitleLine,
                                                InputStream inputStream,
                                                ImportExcelRowDataConverter importExcelRowDateConverter,
                                                ImportExcelRowDataHandler importExcelRowDataHandler,
                                                FileProcessingTaskAware fileProcessingTaskAware,
                                                FileProcessingTaskManager fileProcessingTaskManager) {
        super(name, fileProcessingTaskAware, fileProcessingTaskManager);
        this.inputStream = inputStream;
        this.importExcelRowDateConverter = importExcelRowDateConverter;
        this.importExcelRowDataHandler = importExcelRowDataHandler;
        this.headTitleLine = headTitleLine;
        this.fileProcessingTaskAware = fileProcessingTaskAware;
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
    public void exportFailureFile(OutputStream outputStream) {
        if (outputStream == null) {
            return;
        }

        final ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);

        int size = this.sheets.size();
        for (int i = 0; i < size; i++) {
            Sheet sheet = this.sheets.get(i);
            List<List<String>> head = sheet.getHead();
            if (head == null) {
                continue;
            }
            head.add(Arrays.asList("失败原因"));
//            head.add(Arrays.asList("原始行号"));
            sheet.setHead(head);
            writer.write0(this.failureRows.stream().map(Arrays::asList).collect(Collectors.toList()), sheet);
        }
        writer.finish();

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void process() throws Exception {

        FileProcessingTask fileProcessingTask = this;
        int headTitleLine = this.headTitleLine;
        int headLineMun = headTitleLine - 1;
        log.info("开始导入任务的处理");
        ExcelReader excelReader = EasyExcelFactory.getReader(this.inputStream, new AnalysisEventListener<List<String>>() {
            @Override
            public void invoke(List<String> row, AnalysisContext analysisContext) {
                Sheet currentSheet = analysisContext.getCurrentSheet();
                //跳过表头
//                int headLineMun = currentSheet.getHeadLineMun();
                int currentRowNum = analysisContext.getCurrentRowNum();
                if (currentRowNum <= headLineMun) {
                    log.info("跳过表头");
                    currentSheet.setHeadLineMun(headTitleLine);
                    if (currentSheet.getHead() == null) {
                        List<List<String>> titles = new ArrayList<List<String>>();
                        row.forEach(s -> {
                            List<String> title = new ArrayList<>();
                            title.add(s);
                            titles.add(title);
                        });
                        currentSheet.setHead(titles);
                    }

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
                        addFailureRow(row, "构建数据结果为null", currentRowNum);
                        return;
                    }
                    ExcelRowDataHandleResult excelRowDataHandleResult = importExcelRowDataHandler.handle(data);
                    if (excelRowDataHandleResult.isSuccess()) {
                        increaseSuccessTotal();
                    } else {
                        addFailureRow(row, excelRowDataHandleResult.getFailCause(), currentRowNum);
                    }
                    if (fileProcessingTaskAware != null) {
                        fileProcessingTaskAware.process(fileProcessingTask, data);
                    }
                } catch (Exception e) {
                    log.error("导入处理异常", e);
                    addFailureRow(row, e.getMessage(), currentRowNum);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                //处理完成


            }
        });
        List<Sheet> sheets = excelReader.getSheets();
        this.sheets = sheets.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        this.sheetTotal = sheets.size();
        sheets.forEach(excelReader::read);
    }

    public List<Sheet> getSheets() {
        return Collections.unmodifiableList(sheets);
    }

    public Integer getHeadTitleLine() {
        return headTitleLine;
    }


    /**
     * 创建一个新的sheet 用于导出失败的xlsx
     *
     * @param index
     * @param titles
     * @return
     */
//    protected Sheet createNewSheet(int index, List<List<String>> titles) {
//
//        Sheet sheet = new Sheet(index + 1, this.headTitleLine);
//        sheet.setHead(titles);
//        sheet.setAutoWidth(true);
//        return sheet;
//    }

    /**
     * 添加错误的行记录
     *
     * @param row
     * @param cause              失败原因
     * @param originalLineNumber 原始的行号
     */
    protected void addFailureRow(List<String> row, String cause, int originalLineNumber) {
        List<String> arrayList = new ArrayList<>(row);
        arrayList.add(cause);
//        arrayList.add(originalLineNumber + 1 + "");
        this.failureRows.add(arrayList.toArray(new String[0]));
        increaseFailureTotal();
    }
}
