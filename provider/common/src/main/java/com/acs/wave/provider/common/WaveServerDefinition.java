package com.acs.wave.provider.common;

import com.acs.wave.router.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WaveServerDefinition<SSLContext> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public final String host;
    public final Integer httpPort;
    public final Integer httpsPort;
    public final SSLContext sslContext;
    public final Router router;

    public WaveServerDefinition(String host, Integer httpPort, Integer httpsPort, SSLContext sslContext, Router router) {
        this.host = host;
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
        this.sslContext = sslContext;
        this.router = router;
    }
}
