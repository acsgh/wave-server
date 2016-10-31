package com.acs.waveserver.core.functional;

import com.acs.waveserver.core.HTTPResponseBuilder;
import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;

import java.util.Optional;
import java.util.function.Supplier;

@FunctionalInterface
public interface RequestFilter {
    Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, Supplier<Optional<HTTPResponse>> nextJump);
}
