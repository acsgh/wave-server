package com.acs.wave.router.websocket.request;

public class WebSocketRequestBinary extends WebSocketRequest {

    private final byte[] bytes;

    public WebSocketRequestBinary(String id, String uri, String remoteAddress, byte[] bytes) {
        super(id, uri, remoteAddress);
        this.bytes = bytes;
    }
}
