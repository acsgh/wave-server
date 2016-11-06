package com.acs.wave.provider.common;

import com.acs.wave.router.Router;
import com.acs.wave.utils.CheckUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WaveServerBuilder<T extends WaveServer> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final String LOCAL_HOST = "localhost";
    private static final String DEFAULT_HOST = "0.0.0.0";
    private static final int DEFAULT_HTTP_PORT = 10080;
    private static final int DEFAULT_HTTPS_PORT = 10443;

    protected String host = DEFAULT_HOST;
    protected Integer httpPort = DEFAULT_HTTP_PORT;
    protected Integer httpsPort;
    protected Router router;

    public WaveServerBuilder<T> host(String host) {
        this.host = host;
        return this;
    }

    public WaveServerBuilder<T> defaultHost() {
        this.host = DEFAULT_HOST;
        return this;
    }

    public WaveServerBuilder<T> localHost() {
        this.host = LOCAL_HOST;
        return this;
    }

    public WaveServerBuilder<T> enableHttp(int port) {
        this.httpPort = port;
        return this;
    }

    public WaveServerBuilder<T> defatulHttp() {
        this.httpPort = DEFAULT_HTTP_PORT;
        return this;
    }

    public WaveServerBuilder<T> disableHttp() {
        this.httpPort = null;
        return this;
    }

    public WaveServerBuilder<T> enableHttps(int port) {
        this.httpsPort = port;
        return this;
    }

    public WaveServerBuilder<T> defatulHttps() {
        this.httpPort = DEFAULT_HTTPS_PORT;
        return this;
    }

    public WaveServerBuilder<T> disableHttps() {
        this.httpsPort = null;
        return this;
    }

    public WaveServerBuilder<T> router(Router router) {
        this.router = router;
        return this;
    }

    public T build() {
        CheckUtils.checkNull("router", router);

        if((httpPort == null) && (httpsPort == null)){
            throw new IllegalArgumentException("HTTP or HTTPS should be enable at least");
        }

        return buildInstance();
    }

    protected abstract T buildInstance();
}
