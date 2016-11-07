package com.acs.wave.router.websocket;

public interface WebSocketTextHandler extends WebSocketHandler {

    void onText(WebSocketRequest session, String message);
}
