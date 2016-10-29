package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.RequestMethod;

public class HTTPRequest {
    public final RequestMethod method;
    public final String uri;
    public final ProtocolVersion protocolVersion;

    public HTTPRequest(RequestMethod method, String uri, ProtocolVersion protocolVersion) {
        this.method = method;
        this.uri = uri;
        this.protocolVersion = protocolVersion;
    }

    @Override
    public String toString() {
        return "HTTPRequest{" +
                "method=" + method +
                ", uri='" + uri + '\'' +
                ", protocolVersion=" + protocolVersion +
                '}';
    }
}
