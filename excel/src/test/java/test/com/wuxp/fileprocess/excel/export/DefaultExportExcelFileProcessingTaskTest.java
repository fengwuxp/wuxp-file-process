package test.com.wuxp.fileprocess.excel.export;

import com.wuxp.fileprocess.core.FileProcessingTask;
import com.wuxp.fileprocess.core.FileProcessingTaskAware;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.export.DefaultExportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.export.DefaultExportExcelRowDataConverter;
import com.wuxp.fileprocess.excel.export.ExportExcelDataGrabber;
import com.wuxp.fileprocess.excel.model.ExportExcelCell;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.format.Formatter;
import test.com.wuxp.fileprocess.excel.im.ExampleDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DefaultExportExcelFileProcessingTask Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>8月 22, 2019</pre>
 */
@Slf4j
public class DefaultExportExcelFileProcessingTaskTest {


    private FileProcessingTaskManager fileProcessingTaskManager;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: exportFile(OutputStream outputStream)
     */
    @Test
    public void testExportFile() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: createNewSheet(int index)
     */
    @Test
    public void testCreateNewSheet() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: process()
     */
    @Test
    public void testProcess() throws Exception {
        long l = System.currentTimeMillis();
        List<ExportExcelCell> cells = new ArrayList<>();

        String[] names = {"name", "age", "mobilePhone", "birthday", "isVip", "sex", "balance", "desc"};
        String[] titles = {"姓名", "年龄", "手机号码", "生日", "是否vip", "性别", "余额", "说明"};

        for (int i = 0; i < 7; i++) {
            ExportExcelCell exportExcelCell = new ExportExcelCell();
            exportExcelCell.setValue(names[i]);
            exportExcelCell.setTitle(titles[i]);
            cells.add(exportExcelCell);
        }

        DefaultExportExcelRowDataConverter exportExcelRowDataConverter = new DefaultExportExcelRowDataConverter(cells.stream().map(exportExcelCell -> exportExcelCell.getValue()).collect(Collectors.toList()));


        exportExcelRowDataConverter.setFormatter(5, new Formatter<ExampleDTO.Sex>() {
            @Override
            public ExampleDTO.Sex parse(String text, Locale locale) throws ParseException {
                return null;
            }

            @Override
            public String print(ExampleDTO.Sex object, Locale locale) {
                if (ExampleDTO.Sex.MAN.equals(object)) {
                    return "男";
                } else if (ExampleDTO.Sex.WOMAN.equals(object)) {
                    return "女";
                }
                return "保密";
            }
        });


        DefaultExportExcelFileProcessingTask defaultExportExcelFileProcessingTask = new DefaultExportExcelFileProcessingTask(
                "测试任务",
                cells,
                (ExportExcelDataGrabber<ExampleDTO>) (page, fetchSize) -> {
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
                },
                exportExcelRowDataConverter,
                new FileProcessingTaskAware() {
                    @Override
                    public void preProcess(FileProcessingTask fileProcessingTask) {
                        log.info("开始处理");
                    }

                    @Override
                    public void postProcess(FileProcessingTask fileProcessingTask, FileProcessingTaskManager fileProcessingTaskManager) {
                        log.info("处理完成");
                    }
                }, fileProcessingTaskManager);

        defaultExportExcelFileProcessingTask.run();
        File file = new File(this.getClass().getResource("/").getPath() + "/1.xlsx");
        if (!file.exists()) {
            file.createNewFile();
        }
        defaultExportExcelFileProcessingTask.exportFile(new FileOutputStream(file));

        log.info("运行时间：{}", System.currentTimeMillis() - l);
        file.delete();
    }

    /**
     * Method: writeToCache(List<List<String>> data, Sheet sheet)
     */
    @Test
    public void testWriteToCache() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: converter(T data)
     */
    @Test
    public void testConverter() throws Exception {
//TODO: Test goes here...
    }


}
