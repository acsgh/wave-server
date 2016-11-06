package com.acs.wave.router.functional;

import com.acs.wave.router.HTTPRequest;
import com.acs.wave.router.HTTPResponse;
import com.acs.wave.router.HTTPResponseBuilder;
import com.acs.wave.router.constants.ResponseStatus;

@FunctionalInterface
public interface ErrorCodeHandler {
    HTTPResponse handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, ResponseStatus responseStatus);
}
