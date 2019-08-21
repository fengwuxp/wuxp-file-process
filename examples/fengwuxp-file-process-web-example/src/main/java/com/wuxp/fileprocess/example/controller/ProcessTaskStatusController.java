package com.wuxp.fileprocess.example.controller;


import com.wuxp.fileprocess.core.FileProcessingTask;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.ExcelFileProcessingTask;
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
public class ProcessTaskStatusController {

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

        ExcelProcessStatusDTO dto = new ExcelProcessStatusDTO();

        FileProcessingTask fileProcessingTask = fileProcessingTaskManager.get(taskId);
        if (fileProcessingTask == null) {
            return null;
        }

//        dto.setCurrentSheetIndex(fileProcessingTask.getCurrentSheetIndex())
//                .setName(fileProcessingTask.getName())
//                .setCurrentSheetTotal(fileProcessingTask.getCurrentSheetTotal());

        BeanUtils.copyProperties(fileProcessingTask, dto);

        return dto;
    }


    /**
     * 导出失败文件
     *
     * @param taskId
     * @param response
     * @throws Exception
     */
    @RequestMapping("export_failure")
    public void exportImportFailureFile(String taskId, String fileName, HttpServletResponse response) throws Exception {
        ImportExcelFileProcessingTask fileProcessingTask = (ImportExcelFileProcessingTask) fileProcessingTaskManager.remove(taskId);
        if (fileProcessingTask == null) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("文件不存在或已被下载!");
            return;
        }
        response.setContentType("application/msexcel");
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", MessageFormat.format("attachment; filename={0}", new String(fileName.getBytes(StandardCharsets.UTF_8))));
        fileProcessingTask.exportFailureFile(response.getOutputStream());


    }
}
