package com.acs.wave.provider.common;

import com.acs.wave.router.HTTPRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WaveServerDefinition<SSLContext> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public final String host;
    public final Integer httpPort;
    public final Integer httpsPort;
    public final SSLContext sslContext;
    public final HTTPRouter httpRouter;

    public WaveServerDefinition(String host, Integer httpPort, Integer httpsPort, SSLContext sslContext, HTTPRouter httpRouter) {
        this.host = host;
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
        this.sslContext = sslContext;
        this.httpRouter = httpRouter;
    }

    public boolean hasHTTP() {
        return (httpPort != null);
    }

    public boolean hasHTTPS() {
        return (httpsPort != null) && (sslContext != null);
    }
}
