package com.acs.wave.router.websocket.response;

public class WebSocketResponse {

    public final String id;
    public final String uri;
    public final String remoteAddress;
    public final boolean close;

    public WebSocketResponse(String id, String uri, String remoteAddress, boolean close) {
        this.id = id;
        this.uri = uri;
        this.remoteAddress = remoteAddress;
        this.close = close;
    }
}
