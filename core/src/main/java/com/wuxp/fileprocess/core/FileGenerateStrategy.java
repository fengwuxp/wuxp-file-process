package com.wuxp.fileprocess.core;


/**
 * 文件生成策略
 */
public interface FileGenerateStrategy {


    /**
     * 生成文件
     *
     * @param filename 文件名
     * @return 文件所在路径
     */
    String generate(String filename);
}
