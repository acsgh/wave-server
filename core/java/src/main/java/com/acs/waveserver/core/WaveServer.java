package com.acs.waveserver.core;

import com.sun.deploy.net.HttpRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WaveServer {

    private final List<RequestFilter> filters;
    private final List<RequestHandler> handlers;
    private final Map<ResponseCodes, ErrorCodeHandler> errorCodeHandlers;
    private final ExceptionHandler exceptionHandler;

    public WaveServer(List<RequestFilter> filters, List<RequestHandler> handlers, Map<ResponseCodes, ErrorCodeHandler> errorCodeHandlers, ExceptionHandler exceptionHandler) {
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

    public HTTPResponse process(HttpRequest httpRequest) {
        return null;
    }

    private void checkNotNull(String name, Object value) {
        if (value == null) {
            throw new NullPointerException("Field '" + name + "' is null");
        }
    }
}
