package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.RequestMethod;
import com.acs.waveserver.core.constants.ResponseCode;
import com.acs.waveserver.core.functional.ErrorCodeHandler;
import com.acs.waveserver.core.functional.ExceptionHandler;
import com.acs.waveserver.core.functional.RequestFilter;
import com.acs.waveserver.core.functional.RequestHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouterBuilder {

    private final List<RequestFilter> filters = new ArrayList<>();
    private final List<RequestHandler> handlers = new ArrayList<>();
    private final Map<ResponseCode, ErrorCodeHandler> errorCodeHandlers = getDefaultErrorHandlers();
    private ExceptionHandler exceptionHandler = getDefaultExceptionHandler();


    public Router build() {
        return new Router(filters, handlers, errorCodeHandlers, exceptionHandler);
    }

    public RouterBuilder exceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public RouterBuilder defaultExceptionHandler() {
        this.exceptionHandler = getDefaultExceptionHandler();
        return this;
    }

    public RouterBuilder filter(String url, RequestMethod method, RequestFilter filter) {
        return this;
    }

    public RouterBuilder handler(String url, RequestMethod method, RequestHandler handler) {
        return this;
    }

    private ExceptionHandler getDefaultExceptionHandler() {
        return (request, responseBuilder, throwable) ->{
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
            return responseBuilder.build();
        };
    }

    private Map<ResponseCode, ErrorCodeHandler> getDefaultErrorHandlers() {
        return new HashMap<>();
    }
}
