package com.acs.wave.router.websocket.request;

public class WebSocketRequestBinary extends WebSocketRequest {

    public final byte[] bytes;

    public WebSocketRequestBinary(String id, String uri, String subprotocol, String remoteAddress, byte[] bytes) {
        super(id, uri, subprotocol, remoteAddress);
        this.bytes = bytes;
    }
}
