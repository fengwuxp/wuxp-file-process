package com.wuxp.fileprocess.config;


import lombok.Data;

/**
 * 线程池配置
 */
@Data
public class TaskPoolProperties {

    /**
     * 线程池核心数
     */
    private int corePoolSize = 10;

    /**
     * 线程池 最大数
     */
    private int maxPoolSize = 30;


    /**
     * 存活秒杀
     */
    private int keepAliveSeconds = 180;

    /**
     * 队列最大值
     */
    private int queueCapacity = Integer.MAX_VALUE;

    /**
     * 允许核心线程池超时
     */
    private boolean allowCoreThreadTimeOut = false;

}
