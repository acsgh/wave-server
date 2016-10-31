package com.acs.waveserver.core;

import com.acs.waveserver.core.functional.ErrorCodeHandler;
import com.acs.waveserver.core.functional.RequestHandler;
import com.acs.waveserver.core.utils.ExceptionUtils;
import com.acs.waveserver.core.constants.RequestMethod;
import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.core.functional.ExceptionHandler;
import com.acs.waveserver.core.functional.RequestFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouterBuilder {

    private final List<Route<RequestFilter>> filters = new ArrayList<>();
    private final List<Route<RequestHandler>> handlers = new ArrayList<>();
    private final Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers = new HashMap<>();
    private ErrorCodeHandler defaultErrorCodeHandler;
    private ExceptionHandler exceptionHandler;

    public RouterBuilder() {
        exceptionHandler(null);
        defaultErrorCodeHandler(null);
    }

    public Router build() {
        return new Router(filters, handlers, errorCodeHandlers, defaultErrorCodeHandler, exceptionHandler);
    }

    public RouterBuilder exceptionHandler(ExceptionHandler exceptionHandler) {
        if (exceptionHandler != null) {
            this.exceptionHandler = exceptionHandler;
        } else {
            this.exceptionHandler = getDefaultExceptionHandler();
        }
        return this;
    }

    public RouterBuilder defaultErrorCodeHandler(ErrorCodeHandler defaultErrorCodeHandler) {
        if (defaultErrorCodeHandler != null) {
            this.defaultErrorCodeHandler = defaultErrorCodeHandler;
        } else {
            this.defaultErrorCodeHandler = getDefaultErrorCodeHandler();
        }
        return this;
    }

    public RouterBuilder filter(String url, RequestMethod method, RequestFilter filter) {
        Route<RequestFilter> route = new Route<>(url, method, filter);
        filters.add(route);
        return this;
    }

    public RouterBuilder handler(String url, RequestMethod method, RequestHandler handler) {
        Route<RequestHandler> route = new Route<>(url, method, handler);
        handlers.remove(route);
        handlers.add(route);
        return this;
    }

    public RouterBuilder removeFilter(String url, RequestMethod method) {
        Route<RequestFilter> route = new Route<>(url, method, null);
        filters.remove(route);
        return this;
    }

    public RouterBuilder removeHandler(String url, RequestMethod method) {
        Route<RequestHandler> route = new Route<>(url, method, null);
        filters.remove(route);
        return this;
    }

    private ExceptionHandler getDefaultExceptionHandler() {
        return (request, responseBuilder, throwable) -> {
            responseBuilder.status(ResponseStatus.INTERNAL_SERVER_ERROR);
            responseBuilder.body(ExceptionUtils.stacktraceToHtml(throwable));
            return responseBuilder.build();
        };
    }

    private ErrorCodeHandler getDefaultErrorCodeHandler() {
        return (request, responseBuilder, status) -> {
            responseBuilder.status(status);
            responseBuilder.body(getStatusBody(status));
            return responseBuilder.build();
        };
    }

    private String getStatusBody(ResponseStatus status) {
        String result = "<html>";
        result += "<head>";
        result += "<title>" + status + "</title>";
        result += "</head>";
        result += "<body>";
        result += "<h1>" + status + "</h1>";
        result += "</body>";
        result += "</html>";
        return result;
    }

}
