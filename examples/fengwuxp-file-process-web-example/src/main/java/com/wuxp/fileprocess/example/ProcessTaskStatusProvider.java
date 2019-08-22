package com.wuxp.fileprocess.example;

import com.wuxp.fileprocess.core.FileProcessingTask;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.ExportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.ImportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.model.ExcelProcessStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * 任务处理状态提供者
 */
@Component
@Slf4j
public class ProcessTaskStatusProvider {

    @Autowired
    private FileProcessingTaskManager fileProcessingTaskManager;

    /**
     * 获取任务处理的状态
     *
     * @param taskId
     * @return
     */
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
     * 导出文件
     *
     * @param taskId
     * @param fileName
     * @param response
     * @throws Exception
     */
    public void exportFile(String taskId, String fileName, HttpServletResponse response) throws Exception {
        ExportExcelFileProcessingTask fileProcessingTask = (ExportExcelFileProcessingTask) fileProcessingTaskManager.remove(taskId);
        if (fileProcessingTask == null) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("文件不存在或已被下载!");
            return;
        }
        setResponseHeader(fileName, response);
        fileProcessingTask.exportFile(response.getOutputStream());
    }


    /**
     * 导出导入任务的失败结果文件
     *
     * @param taskId
     * @param response
     * @throws Exception
     */
    public void exportImportFailureFile(String taskId, String fileName, HttpServletResponse response) throws Exception {
        ImportExcelFileProcessingTask fileProcessingTask = (ImportExcelFileProcessingTask) fileProcessingTaskManager.remove(taskId);
        if (fileProcessingTask == null) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("文件不存在或已被下载!");
            return;
        }
        setResponseHeader(fileName, response);
        fileProcessingTask.exportFailureFile(response.getOutputStream());

    }

    private void setResponseHeader(String fileName, HttpServletResponse response) {

        if (StringUtils.hasText(fileName)) {
            fileName = "export_file.xlsx";
        }

        response.setContentType("application/msexcel");
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", MessageFormat.format("attachment; filename={0}", new String(fileName.getBytes(StandardCharsets.UTF_8))));
    }
}
