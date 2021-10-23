package github.stone.sfs.renderer;

import github.stone.sfs.common.Constants;
import github.stone.sfs.utils.IoUtils;
import github.stone.sfs.utils.PropertyUtil;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Html renderer
 *
 * @author stone
 * @date 2021/10/22
 */
public class HtmlRenderer extends AbstractRenderer {

    private final static String URL = Constants.URL + "?path=%s";

    private ByteArrayOutputStream templateEngineProcess(Context context, String templateName) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        ByteArrayOutputStream byteArrayOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
            templateEngine.process(templateName, context, outputStreamWriter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteArrayOutputStream;
    }

    private void setResponse(ByteArrayOutputStream outputStream) {
        ByteArrayInputStream in = IoUtils.outSwapIn(outputStream);
        try {
            response.content().writeBytes(in, outputStream.toByteArray().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
    }

    @Override
    public HttpResponse renderer(File file) {
        Context context = new Context();
        String content = IoUtils.readFile(file);
        context.setVariable("content", content);
        ByteArrayOutputStream outputStream = templateEngineProcess(context, "index");
        setResponse(outputStream);
        return response;
    }

    @Override
    public HttpResponse renderer(List<File> files) {
        List<Map> variables = new ArrayList();
        for (Object file : files) {
            if (file instanceof File) {
                String host = PropertyUtil.getProperty("sfs.host");
                String port = PropertyUtil.getProperty("sfs.port");
                String href = String.format(URL, host, port, ((File) file).getAbsolutePath());
                String fileName = ((File) file).getName();
                Map<String, String> map = new HashMap();
                map.put("fileName", fileName);
                map.put("href", href);
                variables.add(map);
            }
        }
        Context context = new Context();
        context.setVariable("files", variables);
        ByteArrayOutputStream byteArrayOutputStream = templateEngineProcess(context, "index");
        setResponse(byteArrayOutputStream);
        return response;
    }

    @Override
    public HttpResponse error(Throwable throwable) {
        String error = throwable.getLocalizedMessage();
        Context context = new Context();
        context.setVariable("error", error);
        ByteArrayOutputStream outputStream = templateEngineProcess(context, "error");
        setResponse(outputStream);
        return response;
    }
}
