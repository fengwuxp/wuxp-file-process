package test.com.wuxp.fileprocess.excel.im;

import com.wuxp.fileprocess.core.FileProcessingTaskManager;
import com.wuxp.fileprocess.core.enums.ProcessStatus;
import com.wuxp.fileprocess.excel.formatter.ExportCellDataFormatter;
import com.wuxp.fileprocess.excel.im.DefaultImportExcelFileProcessingTask;
import com.wuxp.fileprocess.excel.im.DefaultImportExcelRowDateConverter;
import com.wuxp.fileprocess.excel.model.ExcelRowDataHandleResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.Formatter;
import org.springframework.test.context.junit4.SpringRunner;
import test.com.wuxp.fileprocess.excel.TestFileProcessingApplication;

import java.io.File;
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

//    @Autowired
//    private FileProcessingTaskManager fileProcessingTaskManager;

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


        dtoDefaultImportExcelRowDateConverter.setFormatter("desc", new ExportCellDataFormatter<String,List<String>>() {
            @Override
            public String parse(String cellValue, List<String> rowData) throws ParseException {
                return String.format("%s%s", rowData.get(7), rowData.get(8));
            }
        });

        DefaultImportExcelFileProcessingTask defaultImportExcelFileProcessor = new DefaultImportExcelFileProcessingTask(
                new File(getClass().getResource("/import_example.xlsx").getFile()),
                dtoDefaultImportExcelRowDateConverter,
                data -> {
                    log.info("{}", data);
                    return new ExcelRowDataHandleResult(true, null);
                });

        defaultImportExcelFileProcessor.run();

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
