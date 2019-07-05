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

public class HTTPRouterBuilder {

    protected final List<HTTPRoute<RequestFilter>> filters = new ArrayList<>();
    protected final List<HTTPRoute<RequestHandler>> handlers = new ArrayList<>();
    protected final Map<ResponseStatus, ErrorCodeHandler> errorCodeHandlers = new HashMap<>();
    protected ErrorCodeHandler defaultErrorCodeHandler;
    protected ExceptionHandler exceptionHandler;

    public HTTPRouterBuilder() {
        exceptionHandler(null);
        defaultErrorCodeHandler(null);
    }

    public HTTPRouter build() {
        return new HTTPRouter(filters, handlers, errorCodeHandlers, defaultErrorCodeHandler, exceptionHandler);
    }

    public HTTPRouterBuilder exceptionHandler(ExceptionHandler exceptionHandler) {
        if (exceptionHandler != null) {
            this.exceptionHandler = exceptionHandler;
        } else {
            this.exceptionHandler = getDefaultExceptionHandler();
        }
        return this;
    }

    public HTTPRouterBuilder defaultErrorCodeHandler(ErrorCodeHandler defaultErrorCodeHandler) {
        if (defaultErrorCodeHandler != null) {
            this.defaultErrorCodeHandler = defaultErrorCodeHandler;
        } else {
            this.defaultErrorCodeHandler = getDefaultErrorCodeHandler();
        }
        return this;
    }


    public HTTPRouterBuilder options(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.OPTIONS);
    }

    public HTTPRouterBuilder options(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.OPTIONS);
    }

    public HTTPRouterBuilder get(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.GET);
    }

    public HTTPRouterBuilder get(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.GET);
    }

    public HTTPRouterBuilder head(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.HEAD);
    }

    public HTTPRouterBuilder head(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.HEAD);
    }

    public HTTPRouterBuilder post(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.POST);
    }

    public HTTPRouterBuilder post(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.POST);
    }

    public HTTPRouterBuilder put(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.PUT);
    }

    public HTTPRouterBuilder put(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.PUT);
    }

    public HTTPRouterBuilder patch(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.PATCH);
    }

    public HTTPRouterBuilder patch(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.PATCH);
    }

    public HTTPRouterBuilder delete(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.DELETE);
    }

    public HTTPRouterBuilder delete(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.DELETE);
    }

    public HTTPRouterBuilder trace(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.TRACE);
    }

    public HTTPRouterBuilder trace(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.TRACE);
    }

    public HTTPRouterBuilder connect(String url, RequestFilter filter) {
        return filter(url, filter, RequestMethod.CONNECT);
    }

    public HTTPRouterBuilder connect(String url, RequestHandler handler) {
        return handler(url, handler, RequestMethod.CONNECT);
    }

    public HTTPRouterBuilder filter(String url, RequestFilter filter, RequestMethod... methods) {
        HTTPRoute<RequestFilter> httpRoute = new HTTPRoute<>(url, toSet(methods), filter);
        filters.add(httpRoute);
        return this;
    }

    public HTTPRouterBuilder handler(String url, RequestHandler handler, RequestMethod... methods) {
        HTTPRoute<RequestHandler> httpRoute = new HTTPRoute<>(url, toSet(methods), handler);
        handlers.remove(httpRoute);
        handlers.add(httpRoute);
        return this;
    }

    public HTTPRouterBuilder removeFilter(String url, RequestMethod method) {
        List<HTTPRoute<RequestFilter>> toRemove = filters.stream()
                .filter(httpRoute -> httpRoute.uri.equalsIgnoreCase(url) && httpRoute.methods.contains(method))
                .collect(Collectors.toList());
        filters.removeAll(toRemove);
        return this;
    }

    public HTTPRouterBuilder removeHandler(String url, RequestMethod method) {
        List<HTTPRoute<RequestHandler>> toRemove = handlers.stream()
                .filter(httpRoute -> httpRoute.uri.equalsIgnoreCase(url) && httpRoute.methods.contains(method))
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
