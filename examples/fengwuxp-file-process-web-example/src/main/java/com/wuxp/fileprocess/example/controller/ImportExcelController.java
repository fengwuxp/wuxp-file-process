package com.wuxp.fileprocess.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@RestController
@RequestMapping("import_excel")
public class ImportExcelController {


    @RequestMapping("import_example")
    public String importExample(CommonsMultipartFile file) {

        return null;
    }
}
