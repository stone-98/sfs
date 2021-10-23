package github.stone.sfs;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author candy
 * @date 2021/10/23 17:48
 */
public class ThymeleafTest {
    @Test
    public void test2() throws IOException {
        //构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");//模板文件后缀
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        //构造上下文(Model)
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("text","123");
        map.put("href","234");
        list.add(map);
        Context context = new Context();
        context.setVariable("files",list);

        //渲染模板
        OutputStream outputStream = new ByteOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        templateEngine.process("thymeleaf", context, outputStreamWriter);
        System.out.println(outputStream);
    }

    @Test
    public void test3(){
        OutputStream outputStream = new ByteOutputStream();

    }
}
