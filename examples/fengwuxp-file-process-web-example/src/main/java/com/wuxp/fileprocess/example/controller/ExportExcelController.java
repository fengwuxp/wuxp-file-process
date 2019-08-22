package com.wuxp.fileprocess.example.controller;

import com.wuxp.fileprocess.example.model.ExampleDTO;
import com.wuxp.fileprocess.excel.ExportExcelFIleTaskBuilder;
import com.wuxp.fileprocess.excel.export.DefaultExportExcelRowDataConverter;
import com.wuxp.fileprocess.excel.export.ExportExcelDataGrabber;
import com.wuxp.fileprocess.excel.model.ExportExcelCell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("export_excel")
@Controller
@Slf4j
public class ExportExcelController {

    @Autowired
    private ExportExcelFIleTaskBuilder exportExcelFIleTaskBuilder;


    /**
     * 导出例子
     * @param condaiton1
     * @param condation2
     * @return
     */
    @RequestMapping("example")
    @ResponseBody
    public String exportExcelExample(String condaiton1,String condation2){
        // do some thing

        List<ExportExcelCell> cells = new ArrayList<>();
        //spel
        String[] names = {"name", "age", "mobilePhone", "birthday", "isVip", "sex.name()", "balance", "desc"};
        String[] titles = {"姓名", "年龄", "手机号码", "生日", "是否vip", "性别", "余额", "说明"};

        for (int i = 0; i < 7; i++) {
            ExportExcelCell exportExcelCell = new ExportExcelCell();
            exportExcelCell.setValue(names[i]);
            exportExcelCell.setTitle(titles[i]);
            cells.add(exportExcelCell);
        }

       return exportExcelFIleTaskBuilder.newBuilder()
               .taskName("测试导出")
               .excelCells(cells)
               .exportExcelDataGrabber( (ExportExcelDataGrabber<ExampleDTO>) (page, fetchSize) -> {
                   if (page > 3) {
                       return Collections.EMPTY_LIST;
                   }
                   ArrayList<ExampleDTO> exampleDTOS = new ArrayList<>();
                   for (int i = 0; i < fetchSize; i++) {
                       ExampleDTO exampleDTO = new ExampleDTO();
                       exampleDTO.setAge(i);
                       exampleDTO.setName("张三：" + i);
                       exampleDTO.setBirthday(new Date());
                       exampleDTO.setIsVip(i % 2 == 0);
                       exampleDTO.setSex(ExampleDTO.Sex.NONE);
                       exampleDTO.setDesc("哈哈哈");
                       exampleDTO.setMobilePhone("12345678901");
                       exampleDTO.setBalance((double) i);
                       exampleDTOS.add(exampleDTO);
                   }

                   return exampleDTOS;
               })
               .exportExcelRowDataConverter(  new DefaultExportExcelRowDataConverter(cells.stream().map(exportExcelCell -> exportExcelCell.getValue()).collect(Collectors.toList())))
               .start();

    }

}
