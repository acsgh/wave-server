package com.acs.wave.router.websocket;

public interface WebSocketBinaryHandler extends WebSocketHandler {

    void onBinary(WebSocketRequest session, byte[] bytes);
}
