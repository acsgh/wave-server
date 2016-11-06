package com.acs.wave.router;

import com.acs.wave.router.constants.RequestMethod;
import com.acs.wave.router.constants.ResponseStatus;
import com.acs.wave.router.functional.ErrorCodeHandler;
import com.acs.wave.router.functional.ExceptionHandler;
import com.acs.wave.router.functional.RequestFilter;
import com.acs.wave.router.functional.RequestHandler;
import com.acs.wave.utils.ExceptionUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    public RouterBuilder filter(String url, RequestFilter filter, RequestMethod... methods) {
        Route<RequestFilter> route = new Route<>(url, toSet(methods), filter);
        filters.add(route);
        return this;
    }

    public RouterBuilder handler(String url, RequestHandler handler, RequestMethod... methods) {
        Route<RequestHandler> route = new Route<>(url, toSet(methods), handler);
        handlers.remove(route);
        handlers.add(route);
        return this;
    }

    public RouterBuilder removeFilter(String url, RequestMethod method) {
        List<Route<RequestFilter>> toRemove = filters.stream()
                .filter(route -> route.uri.equalsIgnoreCase(url) && route.methods.contains(method))
                .collect(Collectors.toList());
        filters.removeAll(toRemove);
        return this;
    }

    public RouterBuilder removeHandler(String url, RequestMethod method) {
        List<Route<RequestHandler>> toRemove = handlers.stream()
                .filter(route -> route.uri.equalsIgnoreCase(url) && route.methods.contains(method))
                .collect(Collectors.toList());
        handlers.removeAll(toRemove);
        return this;
    }

    private <T> Set<T> toSet(T... values) {
        return new HashSet<>(Arrays.asList(values));
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
