package com.acs.waveserver.core;

import java.io.IOException;
import java.util.Optional;

@FunctionalInterface
public interface ExceptionHandler {
    HTTPResponse handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, Throwable throwable) throws IOException;
}
