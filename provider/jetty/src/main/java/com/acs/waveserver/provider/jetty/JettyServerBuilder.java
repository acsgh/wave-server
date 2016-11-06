package com.acs.waveserver.provider.jetty;

import com.acs.waveserver.provider.common.WaveServerBuilder;

public final class JettyServerBuilder extends WaveServerBuilder<JettyServer> {

    @Override
    public JettyServer buildInstance() {
        return new JettyServer(host, httpPort, httpsPort, router);
    }
}
