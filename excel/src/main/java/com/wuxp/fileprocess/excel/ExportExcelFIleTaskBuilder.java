package com.wuxp.fileprocess.excel;


import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.export.DefaultExportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.export.ExportExcelDataGrabber;
import com.wuxp.fileprocess.excel.model.ExportExcelCell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ExportExcelFIleTaskBuilder {

    @Autowired
    private FileProcessingTaskManager fileProcessingTaskManager;

    public InnerExportExcelFIleTaskBuilder newBuilder() {

        return new InnerExportExcelFIleTaskBuilder();
    }

    public class InnerExportExcelFIleTaskBuilder {

        /**
         * 任务名称
         */
        private String taskName;

        /**
         * 导出配置
         */
        private List<ExportExcelCell> excelCells;

        /**
         * 数据抓取者
         */
        private ExportExcelDataGrabber exportExcelDataGrabber;

        /**
         * 数据转换者
         */
        private ExportExcelFileProcessingTask.ExportExcelRowDataConverter exportExcelRowDataConverter;

        private InnerExportExcelFIleTaskBuilder() {
        }

        public InnerExportExcelFIleTaskBuilder excelCells(List<ExportExcelCell> excelCells) {
            this.excelCells = excelCells;
            return this;
        }

        public InnerExportExcelFIleTaskBuilder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public InnerExportExcelFIleTaskBuilder exportExcelDataGrabber(ExportExcelDataGrabber exportExcelDataGrabber) {
            this.exportExcelDataGrabber = exportExcelDataGrabber;
            return this;
        }

        public InnerExportExcelFIleTaskBuilder exportExcelRowDataConverter(ExportExcelFileProcessingTask.ExportExcelRowDataConverter exportExcelRowDataConverter) {
            this.exportExcelRowDataConverter = exportExcelRowDataConverter;
            return this;
        }


        /**
         * 启动任务
         *
         * @return
         */
        public String start() {

            return fileProcessingTaskManager.join(new DefaultExportExcelFileProcessingTask(
                    this.taskName,
                    this.excelCells,
                    this.exportExcelDataGrabber,
                    this.exportExcelRowDataConverter));
        }
    }
}