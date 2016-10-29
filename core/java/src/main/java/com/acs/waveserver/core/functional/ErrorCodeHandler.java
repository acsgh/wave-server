package com.acs.waveserver.core.functional;

import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;
import com.acs.waveserver.core.HTTPResponseBuilder;
import com.acs.waveserver.core.constants.ResponseStatus;

import java.io.IOException;
import java.util.Optional;

@FunctionalInterface
public interface ErrorCodeHandler {
    HTTPResponse handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, ResponseStatus responseStatus);
}
