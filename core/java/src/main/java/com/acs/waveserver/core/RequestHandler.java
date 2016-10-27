package com.acs.waveserver.core;

import com.sun.deploy.net.HttpRequest;

import java.io.IOException;
import java.util.Optional;

@FunctionalInterface
public interface RequestHandler {
    Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder) throws IOException;
}
