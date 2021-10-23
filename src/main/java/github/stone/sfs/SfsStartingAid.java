package github.stone.sfs;

import github.stone.sfs.common.Constants;
import github.stone.sfs.model.SfsConfig;
import github.stone.sfs.handler.SfsCoreHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author stone
 * @date 2021/10/22
 */
@Slf4j
public class SfsStartingAid {
    private final ServerBootstrap serverBootstrap;
    private final SfsConfig config;

    public SfsStartingAid() {
        this.config = new SfsConfig();
        this.serverBootstrap = new ServerBootstrap();
        EventLoopGroup prentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        serverBootstrap.group(prentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("sfs-http-decoder", new HttpRequestDecoder());
                        pipeline.addLast("sfs-http-aggregator", new HttpObjectAggregator(65536));
                        pipeline.addLast("sfs-http-encoder", new HttpResponseEncoder());
                        pipeline.addLast("sfs-http-chunked", new ChunkedWriteHandler());
                        pipeline.addLast("sfs-core-handler", new SfsCoreHandler());
                    }
                });
    }

    public void bind() {
        int port = config.getPort();
        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert channelFuture != null;
        channelFuture.channel().closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>) future -> {
            if (future.isSuccess()) {
                log.info("The sfs shutdown.");
                return;
            }
            log.error("The sfs shutdown fail.", future.cause());
        });
        log.info("The sfs starting success. visit website:{}", String.format(Constants.VISIT_WEBSITE, port));
    }
}
