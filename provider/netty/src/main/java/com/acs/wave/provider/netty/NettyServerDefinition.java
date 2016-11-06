package com.acs.wave.provider.netty;

import com.acs.wave.provider.common.WaveServerDefinition;
import com.acs.wave.router.Router;
import io.netty.handler.ssl.SslContext;

class NettyServerDefinition extends WaveServerDefinition<SslContext> {

    NettyServerDefinition(String host, Integer httpPort, Integer httpsPort, SslContext sslContext, Router router) {
        super(host, httpPort, httpsPort, sslContext, router);
    }
}
