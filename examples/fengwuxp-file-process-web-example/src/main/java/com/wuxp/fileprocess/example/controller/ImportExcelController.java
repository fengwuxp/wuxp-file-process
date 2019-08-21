package com.wuxp.fileprocess.example.controller;

import com.wuxp.fileprocess.example.model.ExampleDTO;
import com.wuxp.fileprocess.excel.ImportExcelFileTaskBuilder;
import com.wuxp.fileprocess.excel.formatter.ExportCellDataFormatter;
import com.wuxp.fileprocess.excel.im.DefaultImportExcelRowDateConverter;
import com.wuxp.fileprocess.excel.model.ExcelRowDataHandleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.util.Locale;

/**
 * 导入例子
 */
@RestController
@RequestMapping("import_excel")
@Slf4j
public class ImportExcelController {


    @Autowired
    private ImportExcelFileTaskBuilder importExcelFileTaskBuilder;


    @RequestMapping("import_example")
    public String importExample(@RequestParam("file") CommonsMultipartFile commonsMultipartFile, HttpServletRequest request) throws Exception {

        DefaultImportExcelRowDateConverter<ExampleDTO> dtoDefaultImportExcelRowDateConverter = new DefaultImportExcelRowDateConverter<>(ExampleDTO.class);

        dtoDefaultImportExcelRowDateConverter
                .putFiledName(0, "name")
                .putFiledName(1, "age")
                .putFiledName(2, "mobilePhone")
                .putFiledName(3, "birthday")
                .putFiledName(4, "isVip")
                .putFiledName(5, "sex")
                .putFiledName(6, "balance")
                .putFiledName(7, "desc");

        dtoDefaultImportExcelRowDateConverter.setFormatter("isVip", new Formatter<Boolean>() {
            @Override
            public Boolean parse(String text, Locale locale) throws ParseException {
                return "是".equals(text);
            }

            @Override
            public String print(Boolean object, Locale locale) {
                return null;
            }
        });

        dtoDefaultImportExcelRowDateConverter.setFormatter("sex", new Formatter<ExampleDTO.Sex>() {
            @Override
            public ExampleDTO.Sex parse(String text, Locale locale) throws ParseException {
                return "男".equals(text) ? ExampleDTO.Sex.MAN : ExampleDTO.Sex.WOMAN;
            }

            @Override
            public String print(ExampleDTO.Sex object, Locale locale) {
                return null;
            }
        });

        dtoDefaultImportExcelRowDateConverter.setFormatter("desc",
                (ExportCellDataFormatter<String, String>) (cellValue, rowData) -> String.format("%s%s", rowData.get(7), rowData.get(8)));

//        String filePath = request.getServletContext().getRealPath("/") + commonsMultipartFile.getOriginalFilename();
//        File file = new File(filePath);
//        commonsMultipartFile.transferTo(file);
        // commonsMultipartFile.getFileItem().getInputStream();

        return importExcelFileTaskBuilder.newBuilder()
                .taskName("测试任务")
                .inputStream(commonsMultipartFile.getInputStream())
                .importExcelRowDateConverter(dtoDefaultImportExcelRowDateConverter)
                .importExcelRowDataHandler(data -> {
                    log.info("{}", data);
                    return new ExcelRowDataHandleResult(true, null);
                }).start();
    }
}
