package com.acs.wave.provider.netty;

import com.acs.wave.provider.common.WaveServer;

public final class NettyServer extends WaveServer<NettyServerDefinition> {


    private NettyServerChannel httpServer;
    private NettyServerChannel httpsServer;

    NettyServer(NettyServerDefinition definition) {
        super(definition);
    }

    @Override
    protected void startServer() throws Exception {
        if (definition.hasHTTP()) {
            httpServer = new NettyServerChannel(definition.host, definition.httpPort, null, definition.router);
            httpServer.start();
        }

        if (definition.hasHTTPS()) {
            httpsServer = new NettyServerChannel(definition.host, definition.httpsPort, definition.sslContext, definition.router);
            httpsServer.start();
        }
    }

    @Override
    protected void stopServer() throws Exception {
        if (httpServer != null) {
            httpServer.stop();
        }
        if (httpsServer != null) {
            httpsServer.stop();
        }
    }
}
