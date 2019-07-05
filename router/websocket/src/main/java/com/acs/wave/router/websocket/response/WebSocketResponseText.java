package com.acs.wave.router.websocket.response;

public class WebSocketResponseText extends WebSocketResponse {

    public final String text;

    public WebSocketResponseText(String id, String uri, String remoteAddress, boolean close, String text) {
        super(id, uri, remoteAddress, close);
        this.text = text;
    }
}
