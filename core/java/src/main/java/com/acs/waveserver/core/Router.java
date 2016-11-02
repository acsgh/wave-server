package com.acs.waveserver.core;


import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.core.exception.ParameterException;
import com.acs.waveserver.core.functional.ErrorCodeHandler;
import com.acs.waveserver.core.functional.ExceptionHandler;
import com.acs.waveserver.core.functional.RequestFilter;
import com.acs.waveserver.core.functional.RequestHandler;
import com.acs.waveserver.core.utils.CheckUtils;
import com.acs.waveserver.core.utils.LogLevel;
import com.acs.waveserver.core.utils.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Router {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public final List<Route<RequestFilter>> filters;
    public final List<Route<RequestHandler>> handlers;
    public final Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers;
    public final ErrorCodeHandler defaultErrorCodeHandler;
    public final ExceptionHandler exceptionHandler;

    Router(List<Route<RequestFilter>> filters, List<Route<RequestHandler>> handlers, Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers, ErrorCodeHandler defaultErrorCodeHandler, ExceptionHandler exceptionHandler) {
        CheckUtils.checkNull("filters", filters);
        CheckUtils.checkNull("handlers", handlers);
        CheckUtils.checkNull("errorCodeHandlers", errorCodeHandlers);
        CheckUtils.checkNull("defaultErrorCodeHandler", defaultErrorCodeHandler);
        CheckUtils.checkNull("exceptionHandler", exceptionHandler);

        this.filters = Collections.unmodifiableList(filters);
        this.handlers = Collections.unmodifiableList(handlers);
        this.errorCodeHandlers = Collections.unmodifiableMap(errorCodeHandlers);
        this.defaultErrorCodeHandler = defaultErrorCodeHandler;
        this.exceptionHandler = exceptionHandler;
    }

    public HTTPResponse process(HTTPRequest httpRequest) {
        HTTPResponseBuilder responseBuilder = new HTTPResponseBuilder(httpRequest, this);
        log.debug("Request {} {}", httpRequest.method, httpRequest.uri());
        StopWatch stopWatch = new StopWatch().start();
        try {
            return processFilters(httpRequest, responseBuilder)
                    .orElse(processHandler(httpRequest, responseBuilder)
                            .orElse(getErrorResponse(httpRequest, responseBuilder, ResponseStatus.NOT_FOUND)));
        } catch (ParameterException e) {
            log.debug("Invalid Parameter", e);
            return getErrorResponse(httpRequest, responseBuilder, ResponseStatus.BAD_REQUEST);
        } catch (Exception e) {
            return exceptionHandler.handle(httpRequest, responseBuilder, e);
        } finally {
            stopWatch.printElapseTime("Request " + httpRequest.method + " " + httpRequest.uri(), log, LogLevel.DEBUG);
        }
    }

    HTTPResponse getErrorResponse(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder, ResponseStatus responseStatus) {
        ErrorCodeHandler errorCodeHandler = errorCodeHandlers.getOrDefault(responseStatus, defaultErrorCodeHandler);
        return errorCodeHandler.handle(httpRequest, responseBuilder, responseStatus);
    }

    private Optional<HTTPResponse> processFilters(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder) {
        List<Route<RequestFilter>> routes = filters.stream()
                .filter(route -> route.canApply(httpRequest))
                .collect(Collectors.toList());

        return getSupplier(httpRequest, responseBuilder, routes, 0).get();
    }

    private Supplier<Optional<HTTPResponse>> getSupplier(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder, List<Route<RequestFilter>> routes, int index) {
        if (index < routes.size()) {
            Route<RequestFilter> route = routes.get(index);
            return () -> {
                log.trace("Filter {} {}", route.method, route.uri);
                StopWatch stopWatch = new StopWatch().start();
                try {
                    return route.handler.handle(httpRequest.ofRoute(route), responseBuilder, getSupplier(httpRequest, responseBuilder, routes, index + 1));
                } finally {
                    stopWatch.printElapseTime("Filter " + route.method + " " + route.uri, log, LogLevel.TRACE);
                }
            };
        } else {
            return Optional::empty;
        }
    }


    private Optional<HTTPResponse> processHandler(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder) {
        Optional<Route<RequestHandler>> handleRoute = getRequestHandler(httpRequest);

        return handleRoute.flatMap(route -> {
            log.trace("Handler {} {}", route.method, route.uri);
            StopWatch stopWatch = new StopWatch().start();
            try {
                return route.handler.handle(httpRequest.ofRoute(route), responseBuilder);
            } finally {
                stopWatch.printElapseTime("Handler " + route.method + " " + route.uri, log, LogLevel.TRACE);
            }
        });
    }

    private Optional<Route<RequestHandler>> getRequestHandler(HTTPRequest httpRequest) {
        return handlers.stream()
                .filter(route -> route.canApply(httpRequest))
                .map(Optional::of)
                .reduce(Optional.empty(), (a, b) -> b);
    }
}