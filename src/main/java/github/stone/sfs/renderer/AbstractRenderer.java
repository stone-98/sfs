package github.stone.sfs.renderer;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.File;
import java.util.List;

/**
 * @author stone
 * @date 2021/10/23 19:24
 */
public abstract class AbstractRenderer {

    public final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

    public abstract HttpResponse renderer(File file);

    public abstract HttpResponse renderer(List<File> files);

    public abstract HttpResponse error(Throwable throwable);
}
