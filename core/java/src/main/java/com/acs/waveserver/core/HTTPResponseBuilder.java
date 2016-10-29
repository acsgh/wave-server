package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.ResponseStatus;

import static com.acs.waveserver.core.constants.ResponseStatus.*;

public class HTTPResponseBuilder {

    private ProtocolVersion protocolVersion;
    private ResponseStatus responseStatus;

    public HTTPResponseBuilder(HTTPRequest request) {
        version(request.protocolVersion);
        status(OK);
    }

    public HTTPResponse build() {
        return new HTTPResponse(protocolVersion, responseStatus);
    }

    public HTTPResponseBuilder version(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    public HTTPResponseBuilder status(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        return this;
    }
}
