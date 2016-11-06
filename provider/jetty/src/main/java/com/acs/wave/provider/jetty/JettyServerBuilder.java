package com.acs.wave.provider.jetty;

import com.acs.wave.provider.common.WaveServerBuilder;
import spark.ssl.SslStores;

public final class JettyServerBuilder extends WaveServerBuilder<JettyServer> {

    private static final int DEFAULT_VALUE = -1;

    private SslStores sslStores = getDefaultSSLStores();
    private int maxThreads = DEFAULT_VALUE;
    private int minThreads = DEFAULT_VALUE;
    private int threadTimeoutMillis = DEFAULT_VALUE;
    private int soLingerTime = DEFAULT_VALUE;


    @Override
    public JettyServer buildInstance() {
        JettyServerDefinition definition = new JettyServerDefinition(host, httpPort, httpsPort, sslStores, router, maxThreads, minThreads, threadTimeoutMillis, soLingerTime);
        return new JettyServer(definition);
    }


    public JettyServerBuilder maxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }

    public JettyServerBuilder defaultMaxThreads() {
        return maxThreads(DEFAULT_VALUE);
    }

    public JettyServerBuilder minThreads(int minThreads) {
        this.minThreads = minThreads;
        return this;
    }

    public JettyServerBuilder defaultMinThreads() {
        return minThreads(DEFAULT_VALUE);
    }

    public JettyServerBuilder threadTimeoutMillis(int threadTimeoutMillis) {
        this.threadTimeoutMillis = threadTimeoutMillis;
        return this;
    }

    public JettyServerBuilder defaultThreadTimeoutMillis() {
        return threadTimeoutMillis(DEFAULT_VALUE);
    }

    public JettyServerBuilder soLingerTime(int soLingerTime) {
        this.soLingerTime = soLingerTime;
        return this;
    }

    public JettyServerBuilder defaultSoLingerTime() {
        return soLingerTime(DEFAULT_VALUE);
    }

    public JettyServerBuilder sslContext(SslStores sslStores) {
        this.sslStores = sslStores;
        return this;
    }

    public JettyServerBuilder defaultSSLStores() {
        this.sslStores = getDefaultSSLStores();
        return this;
    }


    private SslStores getDefaultSSLStores() {
        try {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
