package com.acs.wave.provider.netty;

import com.acs.wave.provider.common.WaveServerBuilder;

public final class NettyServerBuilder extends WaveServerBuilder<NettyServer> {

    @Override
    public NettyServer buildInstance() {
        return new NettyServer(host, httpPort, httpsPort, router);
    }
}
