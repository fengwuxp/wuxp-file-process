package com.wuxp.fileprocess.example.controller;


import com.wuxp.fileprocess.core.FileProcessingTask;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.example.ProcessTaskStatusProvider;
import com.wuxp.fileprocess.excel.ExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.ExportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.ImportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.model.ExcelProcessStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * 处理任务状态
 */
@Controller
@RequestMapping("task_status")
@Slf4j
public class ProcessTaskStatusController extends ProcessTaskStatusProvider {

    @Autowired
    private FileProcessingTaskManager fileProcessingTaskManager;

    /**
     * 获取任务处理的状态
     *
     * @param taskId
     * @return
     */
    @RequestMapping("get_process_status")
    @ResponseBody
    public ExcelProcessStatusDTO getProcessStatus(String taskId) {

        return super.getProcessStatus(taskId);
    }

    /**
     * 导出文件
     *
     * @param taskId
     * @param fileName
     * @param response
     * @throws Exception
     */
    @RequestMapping("export_file")
    public void exportFile(String taskId, String fileName, HttpServletResponse response) throws Exception {
        super.exportFile(taskId, fileName, response);
    }


    /**
     * 导出导入任务的失败结果文件
     *
     * @param taskId
     * @param response
     * @throws Exception
     */
    @RequestMapping("export_failure")
    public void exportImportFailureFile(String taskId, String fileName, HttpServletResponse response) throws Exception {
        super.exportImportFailureFile(taskId, fileName, response);
    }
}
