package com.wuxp.fileprocess.excel;


import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.im.DefaultImportExcelFileProcessingTask;
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

    //    @Getter
    public class InnerImportExcelFileTaskBuilder {

        /**
         * 文件流
         */
        private InputStream inputStream;

        /**
         * 任务名称
         */
        private String taskName;


        /**
         * 数据转换器
         */
        private ImportExcelFileProcessingTask.ImportExcelRowDateConverter importExcelRowDateConverter;

        /**
         * 数据处理者
         */
        private ImportExcelFileProcessingTask.ImportExcelRowDataHandler importExcelRowDataHandler;

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
        public String start() {

            assert this.inputStream != null;

            return fileProcessingTaskManager.join(new DefaultImportExcelFileProcessingTask(
                    this.taskName,
                    this.inputStream,
                    this.importExcelRowDateConverter,
                    this.importExcelRowDataHandler));
        }
    }
}
