package test.com.wuxp.fileprocess.excel.script.engine;

import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;

public class ScriptEngineTest {


    @Test
    public void testJavaScriptEngine() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        String script = "function add(a,b) { print(a+b); }";
        engine.eval(script);
        Invocable inv = (Invocable) engine;
        inv.invokeFunction("add", 2, 1);
    }


    @Test
    public void testJavaScriptEngineByFile() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        engine.eval(new java.io.FileReader(ScriptEngineTest.class.getResource("/test.js").getFile()));
        Invocable inv = (Invocable) engine;
        Map<String, Object> map = new HashMap<>(4);
        map.put("text", "测试");
        map.put("name", "张三");
        map.put("age", 22);
        Object result = inv.invokeFunction("converter", map);
        System.out.println(result);
    }
}
