package com.acs.waveserver.provider.netty;

import com.acs.waveserver.provider.common.WaveServerBuilder;

public final class NettyServerBuilder extends WaveServerBuilder<NettyServer> {

    @Override
    public NettyServer buildInstance() {
        return new NettyServer(host, httpPort, httpsPort, router);
    }
}
