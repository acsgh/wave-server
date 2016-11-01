package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.ResponseStatus;

import static com.acs.waveserver.core.constants.ResponseStatus.OK;

public class HTTPResponseBuilder {

    private ProtocolVersion protocolVersion;
    private ResponseStatus responseStatus;
    private HTTPHeaders headers = new HTTPHeaders();
    private Object body;

    HTTPResponseBuilder(HTTPRequest request) {
        version(request.protocolVersion);
        status(OK);
    }

    public HTTPResponse build() {
        return new HTTPResponse(protocolVersion, responseStatus, headers, body);
    }

    public HTTPResponseBuilder header(String key, Object value) {
        headers.add(key, value);
        return this;
    }

    public HTTPResponseBuilder version(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    public HTTPResponseBuilder status(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        return this;
    }

    public HTTPResponseBuilder body(Object body) {
        this.body = body;
        return this;
    }
}
