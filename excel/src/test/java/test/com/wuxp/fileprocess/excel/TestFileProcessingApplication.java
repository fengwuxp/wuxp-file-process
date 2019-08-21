package test.com.wuxp.fileprocess.excel;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.wuxp","test.com.wuxp"})
public class TestFileProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestFileProcessingApplication.class,args);
    }
}
