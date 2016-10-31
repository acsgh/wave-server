package com.acs.waveserver.core.functional;

import com.acs.waveserver.core.HTTPResponseBuilder;
import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;

import java.util.Optional;

@FunctionalInterface
public interface RequestHandler {
    Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder);
}
