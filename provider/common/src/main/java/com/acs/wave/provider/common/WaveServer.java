package com.acs.wave.provider.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WaveServer<T extends WaveServerDefinition> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected final T definition;

    private final AtomicBoolean started = new AtomicBoolean(false);

    protected WaveServer(T definition) {
        this.definition = definition;
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
