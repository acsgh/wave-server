package com.acs.wave.router.websocket.request;

public abstract class WebSocketRequest {

    public final String id;
    public final String uri;
    public final String subprotocol;
    public final String remoteAddress;

    public WebSocketRequest(String id, String uri, String subprotocol, String remoteAddress) {
        this.id = id;
        this.uri = uri;
        this.subprotocol = subprotocol;
        this.remoteAddress = remoteAddress;
    }
}
