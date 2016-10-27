package com.acs.waveserver.core;

import java.io.IOException;
import java.util.Optional;

@FunctionalInterface
public interface RequestHandler {
    Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder) throws IOException;
}
