package com.acs.wave.router.websocket.response;

import com.acs.wave.router.websocket.request.WebSocketRequest;

public class WebSocketResponseBuilder {

    private final String id;
    private final String uri;
    private final String remoteAddress;

    private boolean close;
    private String text;
    private byte[] bytes;

    public WebSocketResponseBuilder(WebSocketRequest request) {
        this.id = request.id;
        this.uri = request.uri;
        this.remoteAddress = request.remoteAddress;
    }

    public WebSocketResponseBuilder close(boolean close) {
        this.close = close;
        return this;
    }

    public WebSocketResponseBuilder text(String text) {
        this.text = text;
        this.bytes = null;
        return this;
    }

    public WebSocketResponseBuilder bytes(byte[] bytes) {
        this.bytes = bytes;
        this.text = null;
        return this;
    }

    public WebSocketResponse build() {
        if (text != null) {
            return new WebSocketResponseText(id, uri, remoteAddress, close, text);
        } else if (bytes != null) {
            return new WebSocketResponseBinary(id, uri, remoteAddress, close, bytes);
        } else {
            return new WebSocketResponse(id, uri, remoteAddress, close);

        }
    }
}
