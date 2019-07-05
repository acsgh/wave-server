package com.acs.wave.router.websocket.request;

public class WebSocketRequestText extends WebSocketRequest {

    public final String text;

    public WebSocketRequestText(String id, String uri, String subprotocol, String remoteAddress, String text) {
        super(id, uri, subprotocol, remoteAddress);
        this.text = text;
    }
}
