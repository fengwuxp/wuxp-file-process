package com.wuxp.fileprocess.excel.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * excel 行数据处理结果
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExcelRowDataHandleResult implements Serializable {

    private static final long serialVersionUID = -2850017133491859456L;

    /**
     * 处理是否成功
     */
    private boolean success;

    /**
     * 失败原因
     */
    private String failCause;
}
