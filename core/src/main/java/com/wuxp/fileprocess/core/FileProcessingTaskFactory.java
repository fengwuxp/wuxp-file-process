package com.wuxp.fileprocess.core;


import java.io.File;

/**
 * 文件处理工厂
 */
public interface FileProcessingTaskFactory {


    /**
     * 文件处理者工厂
     *
     * @param file
     * @return
     */
    FileProcessingTask factory(File file);
}
