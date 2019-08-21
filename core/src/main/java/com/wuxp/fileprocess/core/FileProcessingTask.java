package com.wuxp.fileprocess.core;


import com.wuxp.fileprocess.core.enums.ProcessStatus;

import java.util.Date;

/**
 * 文件处理任务的接口
 * 统一使用异步处理
 */
public interface FileProcessingTask extends Runnable {


    /**
     * 获取文件处理者的名称
     *
     * @return
     */
    String getName();

    /**
     * 获取任务处理的标识
     *
     * @return
     */
    String getProcessIdentifies();

    /**
     * 获取处理状态
     *
     * @return
     */
    ProcessStatus getProcessStatus();


    /**
     * 获取开始处理的事件
     *
     * @return
     */
    Date getProcessBeginTime();


    /**
     * 获取处理结束的时间
     *
     * @return
     */
    Date getProcessEndTime();

    /**
     * 中断处理
     */
    void interruptProcess();

}
