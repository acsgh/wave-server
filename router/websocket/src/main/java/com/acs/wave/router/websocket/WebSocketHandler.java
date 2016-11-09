package com.acs.wave.router.websocket;

import com.acs.wave.router.websocket.request.WebSocketRequest;
import com.acs.wave.router.websocket.response.WebSocketResponse;
import com.acs.wave.router.websocket.response.WebSocketResponseBuilder;

public interface WebSocketHandler {

    WebSocketResponse process(WebSocketRequest request, WebSocketResponseBuilder responseBuilder);
}
