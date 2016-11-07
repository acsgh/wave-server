package com.acs.wave.provider.netty;

import com.acs.wave.router.Router;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
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
        p.addLast(new HttpObjectAggregator(64 * 1024 * 1024));
//        p.addLast(new WebSocketServerProtocolHandler("/websocket", null, true));
        p.addLast(new NettyWebSocketFrameHandler("/websocket", null, true));
//        p.addLast(new WebSocketIndexPageHandler("/websocket"));
        p.addLast(new NettyServerChannelHandler(router));
    }
}