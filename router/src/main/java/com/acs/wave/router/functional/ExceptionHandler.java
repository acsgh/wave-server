package com.acs.wave.router.functional;

import com.acs.wave.router.HTTPRequest;
import com.acs.wave.router.HTTPResponse;
import com.acs.wave.router.HTTPResponseBuilder;

@FunctionalInterface
public interface ExceptionHandler {
    HTTPResponse handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, Throwable throwable);
}
