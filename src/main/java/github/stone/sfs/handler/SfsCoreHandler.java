package github.stone.sfs.handler;

import github.stone.sfs.model.ReadContent;
import github.stone.sfs.reader.FileReader;
import github.stone.sfs.renderer.HtmlRenderer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author stone
 * @date 2021/10/22
 */
@Slf4j
public class SfsCoreHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private HtmlRenderer htmlRenderer;

    private FileReader fileReader;

    public SfsCoreHandler() {
        this.fileReader = new FileReader();
        this.htmlRenderer = new HtmlRenderer();
    }

    private String sanitizeUri(String uri) throws UnsupportedEncodingException {
        if (StringUtil.isNullOrEmpty(uri) || "\\".equals(uri)) {
            throw new IllegalArgumentException("The uri not be empty.");
        }
        uri = URLDecoder.decode(uri, "UTF-8");
        int index = uri.indexOf("?");
        if (index < 0) {
            throw new IllegalArgumentException("The uri Illegal.");
        }
        uri = uri.substring(index + 1);
        int pathIndex = uri.indexOf("path=");
        uri = uri.substring(pathIndex + 5);
        return uri.replace("\\", File.separator);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpResponse response;
        try {
            if (!request.decoderResult().isSuccess()) {
                throw new IllegalStateException("Decoding failed.");
            }
            if (request.method() != HttpMethod.GET) {
                throw new IllegalStateException("Only GET requests are supported.");
            }
            final String uri = request.uri();
            final String path = sanitizeUri(uri);
            ReadContent readContent = fileReader.read(path);
            if (readContent.isDirectory()) {
                response = htmlRenderer.renderer(readContent.getFiles());
            } else {
                response = htmlRenderer.renderer(readContent.getFiles().get(0));
            }
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            response = htmlRenderer.error(e);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

}
