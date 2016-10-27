package com.acs.waveserver.core;

import java.io.IOException;
import java.util.Optional;

@FunctionalInterface
public interface RequestFilter {
    Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, RequestFilter nextFilter) throws IOException;
}
