package com.acs.wave.provider.jetty;

import com.acs.wave.provider.common.WaveServerDefinition;
import com.acs.wave.router.Router;
import spark.ssl.SslStores;

class JettyServerDefinition extends WaveServerDefinition<SslStores> {

    public final int maxThreads;
    public final int minThreads;
    public final int threadTimeoutMillis;
    public final int soLingerTime;

    public JettyServerDefinition(String host, Integer httpPort, Integer httpsPort, SslStores sslStores, Router router, int maxThreads, int minThreads, int threadTimeoutMillis, int soLingerTime) {
        super(host, httpPort, httpsPort, sslStores, router);
        this.maxThreads = maxThreads;
        this.minThreads = minThreads;
        this.threadTimeoutMillis = threadTimeoutMillis;
        this.soLingerTime = soLingerTime;
    }
}
