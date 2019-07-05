package com.acs.wave.provider.netty;

import com.acs.wave.provider.common.WaveServerDefinition;
import com.acs.wave.router.HTTPRouter;
import com.acs.wave.router.WebSocketRouter;
import io.netty.handler.ssl.SslContext;

class NettyServerDefinition extends WaveServerDefinition<SslContext> {

    public final WebSocketRouter webSocketRouter;

    NettyServerDefinition(String host, Integer httpPort, Integer httpsPort, SslContext sslContext, HTTPRouter httpRouter, WebSocketRouter webSocketRouter) {
        super(host, httpPort, httpsPort, sslContext, httpRouter);
        this.webSocketRouter = webSocketRouter;
    }
}
