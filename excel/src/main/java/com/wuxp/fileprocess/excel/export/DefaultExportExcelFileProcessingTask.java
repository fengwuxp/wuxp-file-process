package com.wuxp.fileprocess.excel.export;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.wuxp.fileprocess.core.FileProcessingTaskAware;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.AbstractExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.ExportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.model.ExportExcelCell;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * default export excel processing task
 */
@Slf4j
public class DefaultExportExcelFileProcessingTask extends AbstractExcelFileProcessingTask implements ExportExcelFileProcessingTask {


    /**
     * 一个工作表导出的最大行数
     */
    protected int sheetMaxRows = 65535;

    /**
     * 一个excel文件支持的最大sheet个数
     */
    protected int maxSheetNum = 10;


    /**
     * 一次抓取数据的大小
     */
    protected int fetchSize = 500;

    /**
     * 数据抓取者
     */
    protected ExportExcelDataGrabber exportExcelDataGrabber;

    /**
     * 数据转换者
     */
    protected ExportExcelRowDataConverter exportExcelRowDataConverter;

    /**
     * 列定义
     */
    protected List<ExportExcelCell> excelCells;


    /**
     * 缓存处理结果
     */
    private List<List<List<String>>> cacheResult = new ArrayList();

    /**
     * 缓存 sheets
     */
    private List<Sheet> cacheSheets = new ArrayList<>();


    public DefaultExportExcelFileProcessingTask(String taskName,
                                                List<ExportExcelCell> excelCells,
                                                ExportExcelDataGrabber exportExcelDataGrabber,
                                                ExportExcelRowDataConverter exportExcelRowDataConverter,
                                                FileProcessingTaskAware fileProcessingTaskAware,
                                                FileProcessingTaskManager fileProcessingTaskManager) {
        super(taskName, fileProcessingTaskAware, fileProcessingTaskManager);
        this.exportExcelDataGrabber = exportExcelDataGrabber;
        this.exportExcelRowDataConverter = exportExcelRowDataConverter;
        this.excelCells = excelCells;
        this.fileProcessingTaskAware = fileProcessingTaskAware;
    }


    @Override
    public void exportFile(OutputStream outputStream) {
        if (outputStream == null) {
            return;
        }
        ExcelWriter excelWriter = EasyExcelFactory.getWriter(outputStream);
        List<List<List<String>>> cacheResult = this.cacheResult;
        List<Sheet> cacheSheets = this.cacheSheets;
        for (int i = 0; i < this.cacheResult.size(); i++) {
            excelWriter.write0(cacheResult.get(i), cacheSheets.get(i));
        }
        excelWriter.finish();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Sheet createNewSheet(int index) {

        Sheet sheet = new Sheet(index + 1, 1);

        List<List<String>> titles = excelCells.stream().map(exportExcelCell -> {
            List<String> list = new ArrayList<>();
            list.add(exportExcelCell.getTitle());
            return list;
        }).collect(Collectors.toList());

        sheet.setHead(titles);
        sheet.setHeadLineMun(1);
        if (excelCells.get(0).getWidth() != null) {
            Map<Integer, Integer> columnWidthMap = new HashMap<Integer, Integer>();
            for (int i = 0; i < excelCells.size(); i++) {
                columnWidthMap.put(i, excelCells.get(i).getWidth());
            }
            sheet.setColumnWidthMap(columnWidthMap);
        } else {
            sheet.setAutoWidth(true);
        }


        return sheet;
    }

    @Override
    protected void process() throws Exception {

        int fetchSize = this.fetchSize;
        // 抓取数据
        int totalNumber = (int) exportExcelDataGrabber.getTotalNumber();
        // 如果支持
        exportExcelDataGrabber.assertReject(totalNumber);
        this.processTotal = totalNumber;
        int maxPage = Integer.MAX_VALUE, maxSheetNum = 1;
        int sheetMaxRows = this.sheetMaxRows;
        if (totalNumber >= 0) {
            maxPage = totalNumber / fetchSize + (totalNumber % fetchSize > 0 ? 1 : 0);
            maxSheetNum = totalNumber / sheetMaxRows + (totalNumber % sheetMaxRows > 0 ? 1 : 0);
        }

        assert maxSheetNum <= this.maxSheetNum;
        this.currentSheetIndex = 0;
        Sheet newSheet = this.createNewSheet(this.currentSheetIndex);
        int queryPage = 1;
        List<List<String>> results = new ArrayList<>(maxPage == Integer.MAX_VALUE ? sheetMaxRows : totalNumber);
        ExportExcelDataGrabber exportExcelDataGrabber = this.exportExcelDataGrabber;
        FileProcessingTaskAware fileProcessingTaskAware = this.fileProcessingTaskAware;

        while (maxPage-- >= 0) {
            //抓取数据
            List<Object> list = exportExcelDataGrabber.fetchData(queryPage, fetchSize);
            if (list.isEmpty()) {
                //未查询到数据
                break;
            }

            //转换数据
            for (Object o : list) {
                List<String> rows = exportExcelRowDataConverter.converter(o);
                results.add(rows);
                this.increaseSuccessTotal();
                if (results.size() == sheetMaxRows) {
                    //大于一个sheet的条数，缓存起来
                    this.writeToCache(results, newSheet);
                    this.currentSheetIndex++;
                    assert this.currentSheetIndex <= this.maxSheetNum;
                    newSheet = this.createNewSheet(this.currentSheetIndex);
                    results = new ArrayList<>();
                }
                if (fileProcessingTaskAware != null) {
                    fileProcessingTaskAware.process(this, rows);
                }

            }
            queryPage++;
        }

        //将剩余数据写入缓存
        this.writeToCache(results, newSheet);

    }

    /**
     * 将数据写入缓存
     *
     * @param data
     * @param sheet
     */
    protected void writeToCache(List<List<String>> data, Sheet sheet) {
        cacheResult.add(data);
        cacheSheets.add(sheet);
    }

}
