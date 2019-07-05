package com.acs.wave.provider.netty;

import com.acs.wave.provider.common.WaveServerBuilder;
import com.acs.wave.router.WebSocketRouter;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public final class NettyServerBuilder extends WaveServerBuilder<NettyServer> {

    private SslContext sslContext = getDefaultSSLContext();

    private WebSocketRouter webSocketRouter;

    @Override
    public NettyServer buildInstance() {
        NettyServerDefinition definition = new NettyServerDefinition(host, httpPort, httpsPort, sslContext, httpRouter, webSocketRouter);
        return new NettyServer(definition);
    }

    public NettyServerBuilder webSocketRouter(WebSocketRouter webSocketRouter) {
        this.webSocketRouter = webSocketRouter;
        return this;
    }

    public NettyServerBuilder sslContext(SslContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public NettyServerBuilder defaultSSLContext() {
        this.sslContext = getDefaultSSLContext();
        return this;
    }


    private SslContext getDefaultSSLContext() {
        try {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
