package com.acs.wave.router.websocket;

import java.net.SocketAddress;

public class WebSocketResponse {

    public final String id;
    public final String uri;
    public final SocketAddress localAddress;
    public final SocketAddress remoteAddress;

    public WebSocketResponse(String id, String uri, SocketAddress localAddress, SocketAddress remoteAddress) {
        this.id = id;
        this.uri = uri;
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }
}
