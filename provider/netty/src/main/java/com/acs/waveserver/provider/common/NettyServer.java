package com.acs.waveserver.provider.common;

import com.acs.waveserver.core.Router;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public final class NettyServer extends WaveServer {


    private NettyServerChannel httpServer;
    private NettyServerChannel httpsServer;

    NettyServer(String host, Integer httpPort, Integer httpsPort, Router router) {
        super(host, httpPort, httpsPort, router);
    }

    @Override
    protected void startServer() throws Exception {
        if (httpPort != null) {
            httpServer = new NettyServerChannel(host, httpPort, null, router);
            httpServer.start();
        }
        if (httpsPort != null) {
            httpsServer = new NettyServerChannel(host, httpsPort, getSSLContext(), router);
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

    private SslContext getSSLContext() throws SSLException, CertificateException {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
    }
}
