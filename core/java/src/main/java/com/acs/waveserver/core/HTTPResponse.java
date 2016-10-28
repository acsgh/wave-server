package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.HTTPVersion;

public class HTTPResponse {
    public final HTTPVersion httpVersion;
    public final int statusCode;
    public final String statusMessage;

    HTTPResponse(HTTPVersion httpVersion, int statusCode, String statusMessage) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}

