package com.wuxp.fileprocess.excel;


import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.im.DefaultImportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.model.ExcelRowDataHandleResult;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

@Slf4j
@Component
public class ImportExcelFileTaskBuilder {

    @Autowired
    private FileProcessingTaskManager fileProcessingTaskManager;

    public InnerImportExcelFileTaskBuilder newBuilder() {

        return new InnerImportExcelFileTaskBuilder();

    }

    //    @Getter
    public strictfp class InnerImportExcelFileTaskBuilder {

        private InputStream inputStream;

        private String taskName;

        private ImportExcelFileProcessingTask.ImportExcelRowDateConverter importExcelRowDateConverter;

        private ImportExcelFileProcessingTask.ImportExcelRowDataHandler importExcelRowDataHandler;

        private ImportExcelFileProcessingTask fileProcessingTask;

        private InnerImportExcelFileTaskBuilder() {
        }

        public InnerImportExcelFileTaskBuilder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public InnerImportExcelFileTaskBuilder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public InnerImportExcelFileTaskBuilder importExcelRowDateConverter(ImportExcelFileProcessingTask.ImportExcelRowDateConverter importExcelRowDateConverter) {
            this.importExcelRowDateConverter = importExcelRowDateConverter;
            return this;
        }

        public InnerImportExcelFileTaskBuilder importExcelRowDataHandler(ImportExcelFileProcessingTask.ImportExcelRowDataHandler importExcelRowDataHandler) {
            this.importExcelRowDataHandler = importExcelRowDataHandler;
            return this;
        }


        /**
         * 启动任务
         *
         * @return 任务标识
         */
        public String start() {

            assert this.inputStream != null;

            this.fileProcessingTask = new DefaultImportExcelFileProcessingTask(
                    this.taskName,
                    this.inputStream,
                    this.importExcelRowDateConverter,
                    this.importExcelRowDataHandler);
            return fileProcessingTaskManager.join(this.fileProcessingTask);
        }
    }
}
