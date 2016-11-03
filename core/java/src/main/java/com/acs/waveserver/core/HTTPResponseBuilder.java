package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.core.functional.BodyWriter;

import static com.acs.waveserver.core.constants.ResponseStatus.OK;

public class HTTPResponseBuilder {

    private ProtocolVersion protocolVersion;
    private ResponseStatus responseStatus;
    private HTTPHeaders headers = new HTTPHeaders();
    private Object body;

    private final HTTPRequest request;
    private final Router router;

    HTTPResponseBuilder(HTTPRequest request, Router router) {
        this.request = request;
        this.router = router;

        version(request.protocolVersion);
        status(OK);
    }

    public HTTPResponse build() {
        return new HTTPResponse(protocolVersion, responseStatus, headers, body);
    }

    public HTTPResponse error(ResponseStatus errorCode) {
        return router.getErrorResponse(request, this, errorCode);
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

    public <T> HTTPResponseBuilder body(T body, BodyWriter converter) {
        this.body = converter.write(body);

        if (!headers.containsKey("Content-Type")) {
            header("Content-Type", converter.contentType());
        }

        return this;
    }

}
