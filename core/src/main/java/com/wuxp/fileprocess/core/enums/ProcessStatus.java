package com.wuxp.fileprocess.core.enums;


/**
 * 处理状态
 *
 * @author wuxp
 */
public enum ProcessStatus {


    WAIT("等待处理"),

    PROCESSING("处理中"),

    SUCCESS("处理成功"),

    ERROR("处理失败"),

    PART_SUCCESS("部分成功"),

    INTERRUPT("任务被中断");


    /**
     * 状态描述
     */
    private String desc;

    ProcessStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProcessStatus{");
        sb.append("desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
