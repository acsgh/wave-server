package com.acs.waveserver.provider.netty;

import com.acs.waveserver.core.Router;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Router router;
    private final SslContext sslCtx;

    NettyServerChannelInitializer(Router router, SslContext sslCtx) {
        this.router = router;
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpServerCodec());
        p.addLast(new NettyServerChannelHandler(router));
    }
}