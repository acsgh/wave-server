package com.acs.wave.provider.jetty;

import com.acs.wave.provider.common.WaveServerDefinition;
import com.acs.wave.router.HTTPRouter;
import com.acs.wave.router.WebSocketRouter;
import spark.ssl.SslStores;

class JettyServerDefinition extends WaveServerDefinition<SslStores> {

    public final int maxThreads;
    public final int minThreads;
    public final int threadTimeoutMillis;
    public final int soLingerTime;
    public final WebSocketRouter webSocketRouter;

    public JettyServerDefinition(String host, Integer httpPort, Integer httpsPort, SslStores sslStores, HTTPRouter httpRouter, int maxThreads, int minThreads, int threadTimeoutMillis, int soLingerTime, WebSocketRouter webSocketRouter) {
        super(host, httpPort, httpsPort, sslStores, httpRouter);
        this.maxThreads = maxThreads;
        this.minThreads = minThreads;
        this.threadTimeoutMillis = threadTimeoutMillis;
        this.soLingerTime = soLingerTime;
        this.webSocketRouter = webSocketRouter;
    }
}
