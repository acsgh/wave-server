package com.acs.wave.router.websocket;

import java.util.Optional;

@FunctionalInterface
public interface WebSocketHandler {

    Optional<WebSocketResponse> process(WebSocketRequest request, WebSocketHandlerBuilder responseBuilder);
}
