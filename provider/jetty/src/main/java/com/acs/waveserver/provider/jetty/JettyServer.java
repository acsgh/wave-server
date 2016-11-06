package com.acs.waveserver.provider.jetty;

import com.acs.waveserver.core.Router;
import com.acs.waveserver.provider.common.WaveServer;
import com.acs.waveserver.provider.common.WaveServerServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.concurrent.TimeUnit;

public final class JettyServer extends WaveServer {


    private Server server;

    JettyServer(String host, Integer httpPort, Integer httpsPort, Router router) {
        super(host, httpPort, httpsPort, router);
    }

    @Override
    protected void startServer() throws Exception {
        server = create(0,0,0);
        ServerConnector connector = new ServerConnector(server);
        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        connector.setSoLingerTime(-1);
        connector.setHost(host);
        connector.setPort(httpPort);
        server.addConnector(connector);
        WaveServerServlet sevlet = new WaveServerServlet(router);
        server.setHandler(new JettyHandler(sevlet));
        server.start();
        if (httpPort != null) {
        }
        if (httpsPort != null) {
        }
    }

    public static Server create(int maxThreads, int minThreads, int threadTimeoutMillis) {
        Server server;

        if (maxThreads > 0) {
            int max = (maxThreads > 0) ? maxThreads : 200;
            int min = (minThreads > 0) ? minThreads : 8;
            int idleTimeout = (threadTimeoutMillis > 0) ? threadTimeoutMillis : 60000;

            server = new Server(new QueuedThreadPool(max, min, idleTimeout));
        } else {
            server = new Server();
        }

        return server;
    }

    @Override
    protected void stopServer() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
}
