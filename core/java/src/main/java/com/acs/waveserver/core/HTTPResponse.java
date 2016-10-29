package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.ResponseStatus;

public class HTTPResponse {
    public final ProtocolVersion protocolVersion;
    public final ResponseStatus responseStatus;

    HTTPResponse(ProtocolVersion protocolVersion, ResponseStatus responseStatus) {
        this.protocolVersion = protocolVersion;
        this.responseStatus = responseStatus;
    }

    @Override
    public String toString() {
        return "HTTPResponse{" +
                "protocolVersion=" + protocolVersion +
                ", responseStatus=" + responseStatus +
                '}';
    }
}

