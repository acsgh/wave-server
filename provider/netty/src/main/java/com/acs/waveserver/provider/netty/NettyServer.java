package com.acs.waveserver.provider.netty;

import com.acs.waveserver.core.Router;
import com.acs.waveserver.core.RouterBuilder;
import com.acs.waveserver.core.constants.RequestMethod;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public final class NettyServer {

    public static void main(String[] args) throws Exception {
        Router router = getRouter();
        NettyServer nettyServer = new NettyServer("0.0.0.0", 8080, 8443, router);

        nettyServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(nettyServer::stop));

    }

    private static Router getRouter() {
        RouterBuilder builder = new RouterBuilder();
        builder.filter("/", RequestMethod.GET, (request, responseBuilder, next) -> {
            responseBuilder.body("Hello from filter!");
            responseBuilder.header("filter", "1");
            return next.get();
        });
        builder.handler("/", RequestMethod.GET, (request, responseBuilder) -> {
            responseBuilder.body("Hello from Handler!");
            responseBuilder.header("handler", "1");
            return Optional.of(responseBuilder.build());
        });
        return builder.build();
    }

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String host;
    private final Integer httpPort;
    private final Integer httpsPort;
    private final Router router;

    private final AtomicBoolean started = new AtomicBoolean(false);

    private NettyServerChannel httpServer;
    private NettyServerChannel httpsServer;

    public NettyServer(String host, Integer httpPort, Integer httpsPort, Router router) {
        this.host = host;
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
        this.router = router;
    }

    public void start() throws Exception {
        if (started.compareAndSet(false, true)) {
            if (httpPort != null) {
                httpServer = new NettyServerChannel(host, httpPort, null, router);
                httpServer.start();
            }
            if (httpsPort != null) {
                httpsServer = new NettyServerChannel(host, httpsPort, getSSLContext(), router);
                httpsServer.start();
            }
        }
    }

    public void stop() {
        if (started.compareAndSet(true, false)) {
            try {
                if (httpServer != null) {
                    httpServer.stop();
                }
                if (httpsServer != null) {
                    httpsServer.stop();
                }
            } catch (Exception e) {
                log.error("Unable to stop server", e);
            }
        }
    }

    private SslContext getSSLContext() throws SSLException, CertificateException {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
    }
}
