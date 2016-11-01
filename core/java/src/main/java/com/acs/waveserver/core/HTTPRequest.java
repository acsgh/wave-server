package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.RequestMethod;

public class HTTPRequest extends HTTPItem {

    public final RequestMethod method;
    final HTTPAddress address;

    public HTTPRequest(RequestMethod method, String uri, ProtocolVersion protocolVersion, HTTPHeaders headers) {
        this(method, HTTPAddress.build(uri), protocolVersion, headers);
    }

    public HTTPRequest(RequestMethod method, HTTPAddress address, ProtocolVersion protocolVersion, HTTPHeaders headers) {
        super(protocolVersion, headers);
        this.method = method;
        this.address = address;
    }

    public String uri() {
        return address.uri.getPath();
    }

    public String fullUri() {
        return address.uri.toString();
    }

    public HTTPParams pathParams() {
        return address.pathParams;
    }

    public HTTPParams queryParams() {
        return address.queryParams;
    }

    HTTPRequest ofRoute(Route<?> route) {
        return new HTTPRequest(method, address.ofRoute(route.uri), protocolVersion, headers);
    }

    @Override
    public String toString() {
        return "HTTPRequest{" +
                "method=" + method +
                ", uri='" + uri() + '\'' +
                ", protocolVersion=" + protocolVersion +
                ", headers=" + headers +
                ", pathParams='" + pathParams() + '\'' +
                ", queryParams='" + queryParams() + '\'' +
                '}';
    }
}
