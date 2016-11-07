package com.acs.wave.router;

import com.acs.wave.router.websocket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class WebSocketRouterBuilder extends RouterBuilder {

    public final Map<String, WebSocketHandler> webSocketHandlers = new HashMap<>();

    public WebSocketRouter build() {
        return new WebSocketRouter(filters, handlers, errorCodeHandlers, defaultErrorCodeHandler, exceptionHandler, webSocketHandlers);
    }

    public WebSocketRouterBuilder webSocket(String url, WebSocketHandler handler) {
        webSocketHandlers.put(url, handler);
        return this;
    }

    public WebSocketRouterBuilder removeWebSocket(String url) {
        webSocketHandlers.remove(url);
        return this;
    }

}
