package com.acs.wave.router.websocket.request;

public class WebSocketRequestDisconnect extends WebSocketRequest {

    public WebSocketRequestDisconnect(String id, String uri, String subprotocol, String remoteAddress) {
        super(id, uri, subprotocol, remoteAddress);
    }
}
