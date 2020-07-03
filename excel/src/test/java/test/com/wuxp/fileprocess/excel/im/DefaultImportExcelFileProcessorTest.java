package test.com.wuxp.fileprocess.excel.im;

import com.wuxp.fileprocess.core.FileProcessingTask;
import com.wuxp.fileprocess.core.FileProcessingTaskAware;
import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.excel.formatter.ExportCellDataFormatter;
import com.wuxp.fileprocess.excel.im.DefaultImportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.im.DefaultImportExcelRowDataConverter;
import com.wuxp.fileprocess.excel.model.ExcelRowDataHandleResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 * DefaultImportExcelFileProcessor Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>8月 21, 2019</pre>
 */
@Slf4j
//@SpringBootTest(classes = {TestFileProcessingApplication.class})
//@RunWith(SpringRunner.class)
public class DefaultImportExcelFileProcessorTest {

    @Autowired
    private FileProcessingTaskManager fileProcessingTaskManager;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: interruptProcess()
     */
    @Test
    public void testInterruptProcess() throws Exception {

        //  定义行数据转换为dto对象的规则
        DefaultImportExcelRowDataConverter<ExampleDTO> dtoDefaultImportExcelRowDataConverter = new DefaultImportExcelRowDataConverter<>(ExampleDTO.class);
        dtoDefaultImportExcelRowDataConverter
                .putFiledName(0, "name")
                .putFiledName(1, "age")
                .putFiledName(2, "mobilePhone")
                .putFiledName(3, "birthday")
                .putFiledName(4, "isVip")
                .putFiledName(5, "sex")
                .putFiledName(6, "balance")
                .putFiledName(7, "desc");

        // 转换数据列的类型
        dtoDefaultImportExcelRowDataConverter.setFormatter("isVip", new Formatter<Boolean>() {
            @Override
            public Boolean parse(String text, Locale locale) throws ParseException {
                return "是".equals(text);
            }

            @Override
            public String print(Boolean object, Locale locale) {
                return null;
            }
        });

        dtoDefaultImportExcelRowDataConverter.setFormatter("sex", new Formatter<ExampleDTO.Sex>() {
            @Override
            public ExampleDTO.Sex parse(String text, Locale locale) throws ParseException {
                return "男".equals(text) ? ExampleDTO.Sex.MAN : ExampleDTO.Sex.WOMAN;
            }

            @Override
            public String print(ExampleDTO.Sex object, Locale locale) {
                return null;
            }
        });


        dtoDefaultImportExcelRowDataConverter.setFormatter("desc", new ExportCellDataFormatter<String, List<String>>() {
            @Override
            public String parse(String cellValue, List<String> rowData) throws ParseException {
                return String.format("%s%s", rowData.get(7), rowData.get(8));
            }
        });

        DefaultImportExcelFileProcessingTask defaultImportExcelFileProcessor = new DefaultImportExcelFileProcessingTask(
                new File(getClass().getResource("/import_example.xlsx").getFile()),
                1,
                dtoDefaultImportExcelRowDataConverter,
                data -> {
                    log.info("数据处理-->{}", data);
                    return new ExcelRowDataHandleResult(false, "11223");
                },
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
        File file = new File(this.getClass().getResource("/").getPath() + "/1.xlsx");
        if (!file.exists()) {
            file.createNewFile();
        }
        defaultImportExcelFileProcessor.run();
        FileOutputStream outputStream = new FileOutputStream(file);
        defaultImportExcelFileProcessor.exportFailureFile(outputStream);
        file.delete();

//        String join = fileProcessingTaskManager.join(defaultImportExcelFileProcessor);
//        log.info("id = {}", join);
//
//        Thread.sleep(1 * 1000L);
//
//        Assert.assertNotNull(defaultImportExcelFileProcessor.getProcessEndTime());
//
//        Assert.assertTrue(ProcessStatus.ERROR.equals(defaultImportExcelFileProcessor.getProcessStatus()));
    }

    /**
     * Method: getFailureList()
     */
    @Test
    public void testGetFailureList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getFailureRows()
     */
    @Test
    public void testGetFailureRows() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: run()
     */
    @Test
    public void testRun() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: convert(List<String> row)
     */
    @Test
    public void testConvert() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: handle(T data)
     */
    @Test
    public void testHandle() throws Exception {
//TODO: Test goes here...
    }


}
