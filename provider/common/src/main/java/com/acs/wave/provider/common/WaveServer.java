package com.acs.wave.provider.common;

import com.acs.wave.router.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WaveServer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected final String host;
    protected final Integer httpPort;
    protected final Integer httpsPort;
    protected final Router router;

    private final AtomicBoolean started = new AtomicBoolean(false);

    protected WaveServer(String host, Integer httpPort, Integer httpsPort, Router router) {
        this.host = host;
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
        this.router = router;
    }

    public void start() throws Exception {
        if (started.compareAndSet(false, true)) {
            startServer();
        }
    }


    public void stop() {
        if (started.compareAndSet(true, false)) {
            try {
                stopServer();
            } catch (Exception e) {
                log.error("Unable to stop server", e);
            }
        }
    }

    protected abstract void startServer() throws Exception;

    protected abstract void stopServer() throws Exception;
}
