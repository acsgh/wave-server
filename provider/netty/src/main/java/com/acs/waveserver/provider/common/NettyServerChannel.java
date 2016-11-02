package com.acs.waveserver.provider.common;

import com.acs.waveserver.core.Router;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;

import java.util.concurrent.atomic.AtomicBoolean;

final class NettyServerChannel {

    private final String host;
    private final int port;
    private final SslContext sslContext;
    private final Router router;

    private final AtomicBoolean started = new AtomicBoolean(false);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    NettyServerChannel(String host, int port, SslContext sslContext, Router router) {
        this.host = host;
        this.port = port;
        this.sslContext = sslContext;
        this.router = router;
    }

    void start() throws Exception {
        if (started.compareAndSet(false, true)) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new NettyServerChannelInitializer(router, sslContext));

            channel = b.bind(host, port).sync().channel();
        }
    }

    void stop() throws Exception {
        if (started.compareAndSet(false, true)) {
            channel.close().sync();
            close(bossGroup);
            close(workerGroup);
        }
    }

    private void close(EventLoopGroup eventLoopGroup) {
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
