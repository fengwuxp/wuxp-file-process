package com.wuxp.fileprocess.excel;


import com.wuxp.fileprocess.core.FileProcessingTaskAware;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.im.DefaultImportExcelFileProcessingTask;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
@Component
public class ImportExcelFileTaskBuilder {

    @Autowired
    private FileProcessingTaskManager fileProcessingTaskManager;

    public InnerImportExcelFileTaskBuilder newBuilder() {

        return new InnerImportExcelFileTaskBuilder();

    }

    @Getter
    public class InnerImportExcelFileTaskBuilder implements ExcelProcessTaskBuilder {

        /**
         * 文件流
         */
        private InputStream inputStream;

        /**
         * 任务名称
         */
        private String taskName;

        /**
         * 表头的行数 ,默认：1
         */
        private Integer headTitleLine = 1;


        /**
         * 数据转换器
         */
        private ImportExcelFileProcessingTask.ImportExcelRowDataConverter importExcelRowDateConverter;

        /**
         * 数据处理者
         */
        private ImportExcelFileProcessingTask.ImportExcelRowDataHandler importExcelRowDataHandler;

        /**
         * 任务前后处理
         */
        protected FileProcessingTaskAware fileProcessingTaskAware;

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

        public InnerImportExcelFileTaskBuilder importExcelRowDateConverter(ImportExcelFileProcessingTask.ImportExcelRowDataConverter importExcelRowDateConverter) {
            this.importExcelRowDateConverter = importExcelRowDateConverter;
            return this;
        }

        public InnerImportExcelFileTaskBuilder importExcelRowDataHandler(ImportExcelFileProcessingTask.ImportExcelRowDataHandler importExcelRowDataHandler) {
            this.importExcelRowDataHandler = importExcelRowDataHandler;
            return this;
        }

        public InnerImportExcelFileTaskBuilder headTitleLine(int headTitleLine) {
            this.headTitleLine = headTitleLine;
            return this;
        }

        public InnerImportExcelFileTaskBuilder fileProcessingTaskAware(FileProcessingTaskAware fileProcessingTaskAware) {
            this.fileProcessingTaskAware = fileProcessingTaskAware;
            return this;
        }

        @Override
        public FileProcessingTaskManager getFileProcessingTaskManager() {
            return fileProcessingTaskManager;
        }

        /**
         * 使用文件对象来初始化任务
         *
         * @param file
         * @return
         */
        public InnerImportExcelFileTaskBuilder file(File file) {

            assert file != null;

            try {
                this.inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            this.taskName = file.getName();
            return this;
        }

        /**
         * 启动任务
         *
         * @return 任务标识
         */
        public String start(ExcelProcessTaskFactory factory) {
            assert factory != null;
            ExcelFileProcessingTask task = factory.factory(this);
            return fileProcessingTaskManager.join(task);
        }

        @Override
        public String start() {
            assert this.inputStream != null;
            return this.start((ExcelProcessTaskFactory<InnerImportExcelFileTaskBuilder>) builder ->
                    new DefaultImportExcelFileProcessingTask(
                            builder.getTaskName(),
                            builder.getHeadTitleLine(),
                            builder.getInputStream(),
                            builder.getImportExcelRowDateConverter(),
                            builder.getImportExcelRowDataHandler(),
                            builder.getFileProcessingTaskAware(),
                            fileProcessingTaskManager));
        }
    }


}
