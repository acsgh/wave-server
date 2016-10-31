package com.acs.waveserver.core.functional;

import com.acs.waveserver.core.HTTPResponseBuilder;
import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;

@FunctionalInterface
public interface ExceptionHandler {
    HTTPResponse handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, Throwable throwable);
}
