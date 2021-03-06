package com.wuxp.fileprocess.excel.model;

import com.wuxp.fileprocess.core.enums.ProcessStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * excel状态处理 dto
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class ExcelProcessStatusDTO {

    /**
     * 处理任务的名称
     */
    protected String name;

    /**
     * 任务的标识
     */
    protected String processIdentifies;

    /**
     * 任务的处理状态
     */
    protected ProcessStatus processStatus;

    /**
     * 处理的开始时间
     */
    protected Date processBeginTime;

    /**
     * 处理的结束时间
     */
    protected Date processEndTime;

    /**
     * 处理的的总条数
     */
    protected int processTotal = 0;

    /**
     * 成功条数
     */
    protected int successTotal = 0;

    /**
     * 失败条数
     */
    protected int failureTotal = 0;

    /**
     * sheet 数量
     */
    protected int sheetTotal = 0;

    /**
     * 当前 sheet index
     */
    protected int currentSheetIndex = 0;

    /**
     * 但钱 sheet 条数
     */
    protected int currentSheetTotal = 0;


    /**
     * 任务是否结束
     *
     * @return
     */
    public boolean getEnd() {
        if (ProcessStatus.SUCCESS.equals(processStatus)
                || ProcessStatus.PART_SUCCESS.equals(processStatus)
                || ProcessStatus.ERROR.equals(processStatus)) {
            return true;
        }
        return this.processEndTime != null;
    }

}
