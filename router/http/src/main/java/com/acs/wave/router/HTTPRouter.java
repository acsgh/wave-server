package com.acs.wave.router;


import com.acs.wave.router.constants.ResponseStatus;
import com.acs.wave.router.exception.ParameterException;
import com.acs.wave.router.functional.ErrorCodeHandler;
import com.acs.wave.router.functional.ExceptionHandler;
import com.acs.wave.router.functional.RequestFilter;
import com.acs.wave.router.functional.RequestHandler;
import com.acs.wave.utils.CheckUtils;
import com.acs.wave.utils.LogLevel;
import com.acs.wave.utils.StopWatch;
import com.acs.wave.utils.exception.InvalidParameterFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HTTPRouter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final List<HTTPRoute<RequestFilter>> filters;
    private final List<HTTPRoute<RequestHandler>> handlers;
    private final Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers;
    private final ErrorCodeHandler defaultErrorCodeHandler;
    private final ExceptionHandler exceptionHandler;

    HTTPRouter(List<HTTPRoute<RequestFilter>> filters, List<HTTPRoute<RequestHandler>> handlers, Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers, ErrorCodeHandler defaultErrorCodeHandler, ExceptionHandler exceptionHandler) {
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
        } catch (ParameterException | InvalidParameterFormatException e) {
            log.debug("Invalid Parameter", e);
            return getErrorResponse(httpRequest, responseBuilder, ResponseStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error during request", e);
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
        List<HTTPRoute<RequestFilter>> httpRoutes = filters.stream()
                .filter(httpRoute -> httpRoute.canApply(httpRequest))
                .collect(Collectors.toList());

        return getSupplier(httpRequest, responseBuilder, httpRoutes, 0).get();
    }

    private Supplier<Optional<HTTPResponse>> getSupplier(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder, List<HTTPRoute<RequestFilter>> httpRoutes, int index) {
        if (index < httpRoutes.size()) {
            HTTPRoute<RequestFilter> httpRoute = httpRoutes.get(index);
            return () -> {
                log.trace("Filter {} {}", httpRoute.methods, httpRoute.uri);
                StopWatch stopWatch = new StopWatch().start();
                try {
                    return httpRoute.handler.handle(httpRequest.ofRoute(httpRoute), responseBuilder, getSupplier(httpRequest, responseBuilder, httpRoutes, index + 1));
                } finally {
                    stopWatch.printElapseTime("Filter " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE);
                }
            };
        } else {
            return Optional::empty;
        }
    }


    private Optional<HTTPResponse> processHandler(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder) {
        Optional<HTTPRoute<RequestHandler>> handleRoute = getRequestHandler(httpRequest);

        return handleRoute.flatMap(httpRoute -> {
            log.trace("Handler {} {}", httpRoute.methods, httpRoute.uri);
            StopWatch stopWatch = new StopWatch().start();
            try {
                return httpRoute.handler.handle(httpRequest.ofRoute(httpRoute), responseBuilder);
            } finally {
                stopWatch.printElapseTime("Handler " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE);
            }
        });
    }

    private Optional<HTTPRoute<RequestHandler>> getRequestHandler(HTTPRequest httpRequest) {
        return handlers.stream()
                .filter(httpRoute -> httpRoute.canApply(httpRequest))
                .map(Optional::of)
                .reduce(Optional.empty(), (a, b) -> b);
    }
}