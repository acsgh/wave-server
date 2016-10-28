package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.HTTPVersion;
import com.acs.waveserver.core.constants.ResponseCode;

import static com.acs.waveserver.core.constants.ResponseCode.*;

public class HTTPResponseBuilder {

    private HTTPVersion httpVersion;
    private int statusCode;
    private String statusMessage;

    public HTTPResponseBuilder(HTTPRequest request) {
        version(request.httpVersion);
        status(OK);
    }

    public HTTPResponse build() {
        return new HTTPResponse(httpVersion, statusCode, statusMessage);
    }

    public HTTPResponseBuilder version(HTTPVersion httpVersion) {
        this.httpVersion = httpVersion;
        return this;
    }

    public HTTPResponseBuilder status(ResponseCode responseCode) {
        status(responseCode.code, responseCode.message);
        return this;
    }

    public HTTPResponseBuilder status(int code, String message) {
        statusCode = code;
        statusMessage = message;
        return this;
    }
}
