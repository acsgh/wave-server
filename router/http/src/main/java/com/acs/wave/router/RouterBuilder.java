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

    protected final List<Route<RequestFilter>> filters = new ArrayList<>();
    protected final List<Route<RequestHandler>> handlers = new ArrayList<>();
    protected final Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers = new HashMap<>();
    protected ErrorCodeHandler defaultErrorCodeHandler;
    protected ExceptionHandler exceptionHandler;

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


    public RouterBuilder options(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.OPTIONS);
    }

    public RouterBuilder options(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.OPTIONS);
    }

    public RouterBuilder get(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.GET);
    }

    public RouterBuilder get(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.GET);
    }

    public RouterBuilder head(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.HEAD);
    }

    public RouterBuilder head(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.HEAD);
    }

    public RouterBuilder post(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.POST);
    }

    public RouterBuilder post(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.POST);
    }

    public RouterBuilder put(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.PUT);
    }

    public RouterBuilder put(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.PUT);
    }

    public RouterBuilder patch(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.PATCH);
    }

    public RouterBuilder patch(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.PATCH);
    }

    public RouterBuilder delete(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.DELETE);
    }

    public RouterBuilder delete(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.DELETE);
    }

    public RouterBuilder trace(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.TRACE);
    }

    public RouterBuilder trace(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.TRACE);
    }

    public RouterBuilder connect(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.CONNECT);
    }

    public RouterBuilder connect(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.CONNECT);
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
