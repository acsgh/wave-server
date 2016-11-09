package com.acs.wave.router.websocket.response;

public class WebSocketResponseBinary extends WebSocketResponse {

    public final byte[] bytes;

    public WebSocketResponseBinary(String id, String uri, String remoteAddress, boolean close, byte[] bytes) {
        super(id, uri, remoteAddress, close);
        this.bytes = bytes;
    }
}
