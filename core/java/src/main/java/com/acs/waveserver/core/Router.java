package com.acs.waveserver.core;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Router {

    private final List<RequestFilter> filters;
    private final List<RequestHandler> handlers;
    private final Map<ResponseCodes, ErrorCodeHandler> errorCodeHandlers;
    private final ExceptionHandler exceptionHandler;

    public Router(List<RequestFilter> filters, List<RequestHandler> handlers, Map<ResponseCodes, ErrorCodeHandler> errorCodeHandlers, ExceptionHandler exceptionHandler) {
        checkNotNull("filters", filters);
        checkNotNull("handlers", filters);
        checkNotNull("errorCodeHandlers", filters);
        checkNotNull("exceptionHandler", filters);

        this.filters = Collections.unmodifiableList(filters);
        this.handlers = Collections.unmodifiableList(handlers);
        this.errorCodeHandlers = Collections.unmodifiableMap(errorCodeHandlers);
        this.exceptionHandler = exceptionHandler;
    }

    public void start() {

    }

    public void stop() {

    }

    public HTTPResponse process(HTTPRequest httpRequest) throws IOException {
        HTTPResponseBuilder responseBuilder = new HTTPResponseBuilder();
        try {
            return processFilters(httpRequest, responseBuilder)
                    .orElse(processHandler(httpRequest, responseBuilder)
                            .orElse(getErrorResponse(httpRequest, responseBuilder, ResponseCodes.NOT_FOUND)));
        } catch (Exception e) {
            return exceptionHandler.handle(httpRequest, responseBuilder, e);
        }
    }

    private HTTPResponse getErrorResponse(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder, ResponseCodes notFound) {
        return null;
    }

    private Optional<HTTPResponse> processHandler(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder) {
        return null;
    }

    private Optional<HTTPResponse> processFilters(HTTPRequest httpRequest, HTTPResponseBuilder responseBuilder) {
        return null;
    }

    private void checkNotNull(String name, Object value) {
        if (value == null) {
            throw new NullPointerException("Field '" + name + "' is null");
        }
    }
}
