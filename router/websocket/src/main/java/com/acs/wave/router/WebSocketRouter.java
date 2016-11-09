package com.acs.wave.router;


import com.acs.wave.router.constants.ResponseStatus;
import com.acs.wave.router.functional.ErrorCodeHandler;
import com.acs.wave.router.functional.ExceptionHandler;
import com.acs.wave.router.functional.RequestFilter;
import com.acs.wave.router.functional.RequestHandler;
import com.acs.wave.router.websocket.WebSocketHandler;
import com.acs.wave.utils.CheckUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WebSocketRouter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public final Map<String, WebSocketHandler> webSocketHandlers;

    WebSocketRouter(Map<String, WebSocketHandler> webSocketHandlers) {
        CheckUtils.checkNull("webSocketHandlers", webSocketHandlers);
        this.webSocketHandlers = Collections.unmodifiableMap(webSocketHandlers);
    }

//    public boolean acceptWebsocket(HTTPRequest httpRequest)

//    public  Optional<WebSocketResponse> process(WebSocketRequest webSocketRequest) {
//        WebSocketResponseBuilder responseBuilder = new WebSocketResponseBuilder(webSocketRequest, this);
//        log.debug("Request {}", webSocketRequest.uri);
//        StopWatch stopWatch = new StopWatch().start();
//        try {
//            return processFilters(httpRequest, responseBuilder)
//                    .orElse(processHandler(httpRequest, responseBuilder)
//                            .orElse(getErrorResponse(httpRequest, responseBuilder, ResponseStatus.NOT_FOUND)));
//        } catch (ParameterException | InvalidParameterFormatException e) {
//            log.debug("Invalid Parameter", e);
//            return getErrorResponse(httpRequest, responseBuilder, ResponseStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            log.error("Error during request", e);
//            return exceptionHandler.handle(httpRequest, responseBuilder, e);
//        } finally {
//            stopWatch.printElapseTime("Request " + httpRequest.method + " " + httpRequest.uri(), log, LogLevel.DEBUG);
//        }
//    }

}