package com.acs.wave.provider.jetty;

import com.acs.wave.provider.common.WaveServerBuilder;

public final class JettyServerBuilder extends WaveServerBuilder<JettyServer> {

    @Override
    public JettyServer buildInstance() {
        return new JettyServer(host, httpPort, httpsPort, router);
    }
}
