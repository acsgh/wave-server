package com.acs.wave.router.websocket;

@FunctionalInterface
public interface WebSocketHandlerBuilder {

    WebSocketHandler get(WebSocketRequest session);
}
