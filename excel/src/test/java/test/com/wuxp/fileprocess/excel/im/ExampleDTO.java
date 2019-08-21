package test.com.wuxp.fileprocess.excel.im;

import lombok.Data;

import java.util.Date;

@Data
public class ExampleDTO {

    private String name;

    private Integer age;

    private String mobilePhone;

    private Date birthday;

    private Boolean isVip;

    private Sex sex;

    private Double balance;

    private String desc;

    static enum Sex {
        WOMAN,

        MAN,

        NONE
    }
}


