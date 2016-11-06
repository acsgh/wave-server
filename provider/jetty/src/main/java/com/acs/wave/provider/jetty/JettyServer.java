package com.acs.wave.provider.jetty;

import com.acs.wave.provider.common.WaveServer;
import com.acs.wave.provider.common.WaveServerServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import spark.ssl.SslStores;

import java.util.concurrent.TimeUnit;

public final class JettyServer extends WaveServer<JettyServerDefinition> {

    private Server server;

    JettyServer(JettyServerDefinition definition) {
        super(definition);
    }


    @Override
    protected void startServer() throws Exception {
        server = createServer();
        WaveServerServlet servlet = new WaveServerServlet(definition.router);
        server.setHandler(new JettyHandler(servlet));
        if (definition.hasHTTP()) {
            server.addConnector(getServerConnector());
        }
        if (definition.hasHTTPS()) {
            server.addConnector(getSecureServerConnector());
        }
        server.start();
    }

    @Override
    protected void stopServer() throws Exception {
        if (server != null) {
            server.stop();
        }
    }


    private ServerConnector getServerConnector() {
        ServerConnector connector = new ServerConnector(server);
        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        connector.setSoLingerTime(definition.soLingerTime);
        connector.setHost(definition.host);
        connector.setPort(definition.httpPort);
        return connector;
    }

    private Server createServer() {
        Server server;

        if (definition.maxThreads > 0) {
            int max = (definition.maxThreads > 0) ? definition.maxThreads : 200;
            int min = (definition.minThreads > 0) ? definition.minThreads : 8;
            int idleTimeout = (definition.threadTimeoutMillis > 0) ? definition.threadTimeoutMillis : 60000;

            server = new Server(new QueuedThreadPool(max, min, idleTimeout));
        } else {
            server = new Server();
        }

        return server;
    }

    private ServerConnector getSecureServerConnector() {
        SslStores sslStores = definition.sslContext;

        SslContextFactory sslContextFactory = new SslContextFactory(sslStores.keystoreFile());

        if (sslStores.keystorePassword() != null) {
            sslContextFactory.setKeyStorePassword(sslStores.keystorePassword());
        }

        if (sslStores.trustStoreFile() != null) {
            sslContextFactory.setTrustStorePath(sslStores.trustStoreFile());
        }

        if (sslStores.trustStorePassword() != null) {
            sslContextFactory.setTrustStorePassword(sslStores.trustStorePassword());
        }

        ServerConnector connector = new ServerConnector(server, sslContextFactory);
        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        connector.setSoLingerTime(definition.soLingerTime);
        connector.setHost(definition.host);
        connector.setPort(definition.httpsPort);
        return connector;
    }
}
