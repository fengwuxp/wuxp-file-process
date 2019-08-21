package com.wuxp.fileprocess.excel;

import com.wuxp.fileprocess.core.enums.ProcessStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
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

    public AbstractExcelFileProcessingTask(String name) {
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
    public void interruptProcess() {
        log.info("not support");
    }

    @Override
    public void run() {

        this.processBeginTime = new Date();
        this.processStatus = ProcessStatus.PROCESSING;
        try {
            this.process();
            this.processStatus = ProcessStatus.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("处理excel异常", e);
            this.processStatus = ProcessStatus.ERROR;
        }

        this.processEndTime = new Date();
    }

    protected abstract void process() throws Exception;

    protected void increaseSuccessTotal() {
        this.successTotal++;
    }

    protected void increaseFailureTotal() {
        this.failureTotal++;
    }
}
