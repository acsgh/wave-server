package com.acs.wave.router.functional;

import com.acs.wave.router.HTTPRequest;
import com.acs.wave.router.HTTPResponse;
import com.acs.wave.router.HTTPResponseBuilder;

import java.util.Optional;

@FunctionalInterface
public interface RequestHandler {
    Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder);
}
