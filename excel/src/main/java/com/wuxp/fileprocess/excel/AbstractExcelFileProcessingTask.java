package com.wuxp.fileprocess.excel;

import com.wuxp.fileprocess.core.FileProcessingTaskAware;
import com.wuxp.fileprocess.core.enums.ProcessStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 抽象的 excel file processing task
 */
@Slf4j
public abstract class AbstractExcelFileProcessingTask implements ExcelFileProcessingTask {

    protected String name;

    protected String processIdentifies;

    protected ProcessStatus processStatus = ProcessStatus.WAIT;

    protected Date processBeginTime;

    protected Date processEndTime;

    protected int processTotal = 0;

    protected int successTotal = 0;

    protected int failureTotal = 0;

    protected int sheetTotal = 0;

    protected int currentSheetIndex = -1;

    protected int currentSheetTotal = -1;

    protected FileProcessingTaskAware fileProcessingTaskAware;

    /**
     * 每列的样式设置
     */
    protected Map<Integer, CellStyle> cellStyleMap = new HashMap<Integer, CellStyle>();

    public AbstractExcelFileProcessingTask(String name) {
        this(name, null);
    }

    public AbstractExcelFileProcessingTask(String name, FileProcessingTaskAware fileProcessingTaskAware) {

        this.name = name;
        this.processIdentifies = UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProcessIdentifies() {
        return processIdentifies;
    }

    @Override
    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    @Override
    public Date getProcessBeginTime() {
        return processBeginTime;
    }

    @Override
    public Date getProcessEndTime() {
        return processEndTime;
    }

    @Override
    public int getProcessTotal() {
        return processTotal;
    }

    @Override
    public int getSuccessTotal() {
        return successTotal;
    }

    @Override
    public int getFailureTotal() {
        return failureTotal;
    }

    @Override
    public int getSheetTotal() {
        return this.sheetTotal;
    }

    @Override
    public int getCurrentSheetIndex() {
        return this.currentSheetIndex;
    }

    @Override
    public int getCurrentSheetTotal() {
        return this.currentSheetTotal;
    }


    @Override
    public ExcelFileProcessingTask put(int index, CellStyle cellStyle) {
        this.cellStyleMap.put(index, cellStyle);
        return this;
    }

    @Override
    public void setCellStyles(Map<Integer, CellStyle> cellStyles) {
        this.cellStyleMap = cellStyles;
    }

    @Override
    public void interruptProcess() {
        log.info("not support");
    }

    @Override
    public void run() {

        FileProcessingTaskAware fileProcessingTaskAware = this.fileProcessingTaskAware;
        boolean needHandleAware = fileProcessingTaskAware != null;
        if (needHandleAware) {
            fileProcessingTaskAware.preProcess(this);
        }

        this.processBeginTime = new Date();
        this.processStatus = ProcessStatus.PROCESSING;
        try {
            this.process();
            if (this.failureTotal == 0) {
                this.processStatus = ProcessStatus.SUCCESS;
            } else if (this.successTotal == 0) {
                this.processStatus = ProcessStatus.ERROR;
            } else {
                this.processStatus = ProcessStatus.PART_SUCCESS;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("处理excel异常", e);
            this.processStatus = ProcessStatus.ERROR;
        }

        if (needHandleAware) {
            fileProcessingTaskAware.postProcess(this);
        }
        this.processEndTime = new Date();
        log.info("任务处理结束 {}", this.name);
    }

    protected abstract void process() throws Exception;

    protected void increaseSuccessTotal() {
        this.successTotal++;
    }

    protected void increaseFailureTotal() {
        this.failureTotal++;
    }
}
