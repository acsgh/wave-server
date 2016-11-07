package com.acs.wave.router.functional;

import com.acs.wave.router.HTTPRequest;
import com.acs.wave.router.HTTPResponse;
import com.acs.wave.router.HTTPResponseBuilder;

import java.util.Optional;
import java.util.function.Supplier;

@FunctionalInterface
public interface RequestFilter {
    Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, Supplier<Optional<HTTPResponse>> nextJump);
}
