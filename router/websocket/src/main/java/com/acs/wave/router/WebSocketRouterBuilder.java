package com.acs.wave.router;

import com.acs.wave.router.websocket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class WebSocketRouterBuilder {

    public final Map<WebSocketRoute, WebSocketHandler> webSocketHandlers = new HashMap<>();

    public WebSocketRouter build() {
        return new WebSocketRouter(webSocketHandlers);
    }

    public WebSocketRouterBuilder webSocket(String uri, WebSocketHandler handler) {
        return webSocket(uri, null, handler);
    }

    public WebSocketRouterBuilder webSocket(String uri, String subprotocols, WebSocketHandler handler) {
        webSocketHandlers.put(new WebSocketRoute(uri, subprotocols), handler);
        return this;
    }

    public WebSocketRouterBuilder removeWebSocket(String uri, String subprotocols) {
        webSocketHandlers.remove(new WebSocketRoute(uri, subprotocols));
        return this;
    }

}
