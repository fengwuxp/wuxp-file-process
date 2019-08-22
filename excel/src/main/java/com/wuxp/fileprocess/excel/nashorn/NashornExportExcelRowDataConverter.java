package com.wuxp.fileprocess.excel.nashorn;

import com.coveo.nashorn_modules.FilesystemFolder;
import com.wuxp.fileprocess.excel.ExportExcelFileProcessingTask;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.File;
import java.util.List;

/**
 * 使用 nashorn Js引擎来处理数据
 */
@Slf4j
public class NashornExportExcelRowDataConverter implements ExportExcelFileProcessingTask.ExportExcelRowDataConverter {

    private NashornSandbox nashornSandbox = NashornSandboxes.create();

    /**
     * js 文件路径，本地或远程
     * 暂时只支持本地
     */
    private String filename;

    private String basePath;


    @Override
    public List<String> converter(Object data) {

        Object eval = null;
        try {
            String javascript = this.loadLocalJs();
            eval = nashornSandbox.eval(javascript, this.createContext(data));
        } catch (ScriptException e) {
            e.printStackTrace();
            log.error("执行js脚本异常", e);
        }

        if (eval == null) {
            return null;
        }

        Object call = ((ScriptObjectMirror) eval).call(this);
        if (call == null) {
            return null;
        }
        return (List<String>) call;
    }

    /**
     * 加载本地js 文件
     *
     * @return
     */
    protected String loadLocalJs() {

        FilesystemFolder rootFolder = FilesystemFolder.create(new File(this.basePath), "UTF-8");

        return rootFolder.getFile(this.filename);
    }

    protected ScriptContext createContext(Object data) {

        ScriptContext context=new SimpleScriptContext();



//        context.setAttribute();


        return context;
    }
}
