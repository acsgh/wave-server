package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.RequestMethod;

public class HTTPRequest extends HTTPItem {

    public final RequestMethod method;
    public final HTTPUri uri;

    public HTTPRequest(RequestMethod method, String uri, ProtocolVersion protocolVersion, HTTPHeaders headers) {
        this(method, HTTPUri.build(uri), protocolVersion, headers);
    }

    public HTTPRequest(RequestMethod method, HTTPUri uri, ProtocolVersion protocolVersion, HTTPHeaders headers) {
        super(protocolVersion, headers);
        this.method = method;
        this.uri = uri;
    }

    HTTPRequest ofRoute(Route<?> route) {
        return new HTTPRequest(method, uri.ofRoute(route), protocolVersion, headers);
    }


    @Override
    public String toString() {
        return "HTTPRequest{" +
                "method=" + method +
                ", uri='" + uri + '\'' +
                ", protocolVersion=" + protocolVersion +
                ", headers=" + headers +
                '}';
    }
}
