package com.acs.wave.router;


import com.acs.wave.router.websocket.WebSocketHandler;
import com.acs.wave.router.websocket.request.WebSocketRequest;
import com.acs.wave.router.websocket.response.WebSocketResponse;
import com.acs.wave.router.websocket.response.WebSocketResponseBuilder;
import com.acs.wave.utils.CheckUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class WebSocketRouter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<WebSocketRoute, WebSocketHandler> webSocketHandlers;

    WebSocketRouter(Map<WebSocketRoute, WebSocketHandler> webSocketHandlers) {
        CheckUtils.checkNull("webSocketHandlers", webSocketHandlers);
        this.webSocketHandlers = Collections.unmodifiableMap(webSocketHandlers);
    }

    public Set<WebSocketRoute> routes() {
        return webSocketHandlers.keySet();
    }

    public WebSocketResponse handle(WebSocketRequest request) {
        WebSocketResponseBuilder responseBuilder = new WebSocketResponseBuilder(request);

        WebSocketHandler handler = webSocketHandlers.getOrDefault(new WebSocketRoute(request.uri, request.subprotocol), this::defaultHandler);

        return handler.handle(request, responseBuilder);
    }

    private WebSocketResponse defaultHandler(WebSocketRequest request, WebSocketResponseBuilder responseBuilder) {
        return responseBuilder.build();
    }
}