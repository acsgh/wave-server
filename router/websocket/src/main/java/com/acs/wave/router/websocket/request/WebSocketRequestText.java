package com.acs.wave.router.websocket.request;

public class WebSocketRequestText extends WebSocketRequest {

    private final String text;

    public WebSocketRequestText(String id, String uri, String remoteAddress, String text) {
        super(id, uri, remoteAddress);
        this.text = text;
    }
}
