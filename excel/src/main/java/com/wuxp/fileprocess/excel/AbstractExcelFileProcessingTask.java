package com.wuxp.fileprocess.excel;

import com.wuxp.fileprocess.core.FileProcessingTaskAware;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.core.enums.ProcessStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 抽象的 excel file processing task
 *
 * @author wuxp
 */
@Slf4j
public abstract class AbstractExcelFileProcessingTask implements ExcelFileProcessingTask {

    protected final String name;

    protected final String processIdentifies;

    protected volatile ProcessStatus processStatus = ProcessStatus.WAIT;

    protected volatile Date processBeginTime;

    protected volatile Date processEndTime;

    protected volatile int processTotal = 0;

    protected volatile int successTotal = 0;

    protected volatile int failureTotal = 0;

    protected volatile int sheetTotal = 0;

    protected volatile int currentSheetIndex = -1;

    protected volatile int currentSheetTotal = -1;

    protected FileProcessingTaskAware fileProcessingTaskAware;


    protected FileProcessingTaskManager fileProcessingTaskManager;

    /**
     * 每列的样式设置
     */
    protected Map<Integer, CellStyle> cellStyleMap = new HashMap<Integer, CellStyle>();


    public AbstractExcelFileProcessingTask(String name,
                                           FileProcessingTaskAware fileProcessingTaskAware,
                                           FileProcessingTaskManager fileProcessingTaskManager) {

        this.name = name;
        this.fileProcessingTaskAware = fileProcessingTaskAware;
        this.fileProcessingTaskManager = fileProcessingTaskManager;
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
        Date beginTime = new Date();
        this.processBeginTime = beginTime;
        if (log.isInfoEnabled()) {
            log.info("开始处理name={}的任务", this.name);
        }
        FileProcessingTaskAware fileProcessingTaskAware = this.fileProcessingTaskAware;
        boolean needHandleAware = fileProcessingTaskAware != null;
        if (needHandleAware) {
            fileProcessingTaskAware.preProcess(this);
        }

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
            if (log.isInfoEnabled()) {
                log.info("处理excel异常,message={}", e.getMessage(), e);
            }
            this.processStatus = ProcessStatus.ERROR;
        }

        if (needHandleAware) {
            fileProcessingTaskAware.postProcess(this, fileProcessingTaskManager);
        }
        Date endTime = new Date();
        this.processEndTime = endTime;
        if (log.isInfoEnabled()) {
            log.info("处理name={}的导出结束，耗时={}", this.name, endTime.getTime() - beginTime.getTime());
        }
    }


    protected abstract void process() throws Exception;

    protected void increaseSuccessTotal() {
        this.successTotal++;
    }

    protected void increaseFailureTotal() {
        this.failureTotal++;
    }
}
