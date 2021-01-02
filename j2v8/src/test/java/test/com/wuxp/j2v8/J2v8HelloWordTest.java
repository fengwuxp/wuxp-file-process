package test.com.wuxp.j2v8;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class J2v8HelloWordTest {


    @Test
    public void testJ2v8() {
        V8 runtime = V8.createV8Runtime();
        int result = runtime.executeIntegerScript(""
                + "const hello = 'hello, ';\n"
                + "const world = 'world!';\n"
                + "hello.concat(world).length;\n");
        System.out.println(result);
        runtime.release();
    }

    @Test
    public void testEs6() throws Exception {
        V8 runtime = V8.createV8Runtime();
        String filepath = J2v8HelloWordTest.class.getResource("/es6_test.js").getFile();
        String es6Script = FileUtils.readFileToString(new File(filepath), StandardCharsets.UTF_8);
        long begin = System.nanoTime();
        Object result = runtime.executeScript(es6Script);
        System.out.println(System.nanoTime() - begin);
        begin = System.nanoTime();
        String helloWord = this.sayHelloWord();
        System.out.println(System.nanoTime() - begin);
        System.out.println(result+" "+helloWord);
        System.out.println(result.getClass());
        runtime.release();
    }

    @Test
    public void testNodeJs() throws Exception {
        String filepath = J2v8HelloWordTest.class.getResource("/es6_test.js").getFile();
        File file = new File(filepath);
        NodeJS nodeJS = NodeJS.createNodeJS();
        nodeJS.exec(file);
        nodeJS.release();
    }

    private String sayHelloWord(){
        return "hello word v8";
    }
}
