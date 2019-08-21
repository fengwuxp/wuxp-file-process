package com.wuxp.fileprocess.example.excel.model;

import com.wuxp.fileprocess.core.enums.ProcessStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * excel状态处理 dto
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

}
