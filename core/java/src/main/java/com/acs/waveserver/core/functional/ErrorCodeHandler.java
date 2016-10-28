package com.acs.waveserver.core.functional;

import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;
import com.acs.waveserver.core.HTTPResponseBuilder;

import java.io.IOException;
import java.util.Optional;

@FunctionalInterface
public interface ErrorCodeHandler {
    HTTPResponse handle(HTTPRequest request, HTTPResponseBuilder responseBuilder) throws IOException;
}
