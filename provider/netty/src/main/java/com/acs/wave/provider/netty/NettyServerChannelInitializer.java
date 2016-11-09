package com.acs.wave.provider.netty;

import com.acs.wave.router.HTTPRouter;
import com.acs.wave.router.WebSocketRoute;
import com.acs.wave.router.WebSocketRouter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final HTTPRouter httpRouter;
    private final WebSocketRouter webSocketRouter;
    private final SslContext sslCtx;

    NettyServerChannelInitializer(HTTPRouter httpRouter, WebSocketRouter webSocketRouter, SslContext sslCtx) {
        this.httpRouter = httpRouter;
        this.webSocketRouter = webSocketRouter;
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

        if (webSocketRouter != null)
            for (WebSocketRoute route : webSocketRouter.routes()) {
                p.addLast(new NettyWebSocketFrameHandler(webSocketRouter, route.uri, route.subprotocol));
            }

        p.addLast(new NettyServerChannelHandler(httpRouter));
    }
}