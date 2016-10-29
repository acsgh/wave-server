package com.acs.waveserver.core;


import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.core.functional.ErrorCodeHandler;
import com.acs.waveserver.core.functional.ExceptionHandler;
import com.acs.waveserver.core.functional.RequestFilter;
import com.acs.waveserver.core.functional.RequestHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Router {

    public final List<RequestFilter> filters;
    public final List<RequestHandler> handlers;
    public final Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers;
    public final ErrorCodeHandler defaultErrorCodeHandler;
    public final ExceptionHandler exceptionHandler;

    Router(List<RequestFilter> filters, List<RequestHandler> handlers, Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers, ErrorCodeHandler defaultErrorCodeHandler, ExceptionHandler exceptionHandler) {
        checkNotNull("filters", filters);
        checkNotNull("handlers", handlers);
        checkNotNull("errorCodeHandlers", errorCodeHandlers);
        checkNotNull("defaultErrorCodeHandler", defaultErrorCodeHandler);
        checkNotNull("exceptionHandler", exceptionHandler);

        this.filters = Collections.unmodifiableList(filters);
        this.handlers = Collections.unmodifiableList(handlers);
        this.errorCodeHandlers = Collections.unmodifiableMap(errorCodeHandlers);
        this.defaultErrorCodeHandler = defaultErrorCodeHandler;
        this.exceptionHandler = exceptionHandler;
    }

    public HTTPResponse process(HTTPRequest httpRequest) {
        HTTPResponseBuilder responseBuilder = new HTTPResponseBuilder(httpRequest);
        try {
            return processFilters(httpRequest, responseBuilder)
                    .orElse(processHandler(httpRequest, responseBuilder)
                            .orElse(getErrorResponse(httpRequest, responseBuilder, ResponseStatus.NOT_FOUND)));
        } catch (Exception e) {
            return exceptionHandler.handle(httpRequest, responseBuilder, e);
        }
    }

    private HTTPResponse getErrorResponse(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder, ResponseStatus responseStatus) {
        ErrorCodeHandler errorCodeHandler = errorCodeHandlers.getOrDefault(responseStatus, defaultErrorCodeHandler);
        return errorCodeHandler.handle(httpRequest, responseBuilder, responseStatus);
    }

    private Optional<HTTPResponse> processHandler(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder) {
        return Optional.empty();
    }

    private Optional<HTTPResponse> processFilters(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder) {
        return Optional.empty();
    }

    private void checkNotNull(String name, Object value) {
        if (value == null) {
            throw new NullPointerException("Field '" + name + "' is null");
        }
    }
}
