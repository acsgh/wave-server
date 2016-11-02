package com.acs.waveserver.provider.common;

public final class NettyServerBuilder extends WaveServerBuilder<NettyServer>{

    @Override
    public NettyServer buildInstance() {
        return new NettyServer(host, httpPort, httpsPort, router);
    }
}
