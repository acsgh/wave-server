package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.RequestMethod;
import com.acs.waveserver.core.functional.BodyReader;

public class HTTPRequest extends HTTPItem {

    public final RequestMethod method;
    final HTTPAddress address;
    private final String body;

    public HTTPRequest(RequestMethod method, String uri, ProtocolVersion protocolVersion, HTTPHeaders headers, String body) {
        this(method, HTTPAddress.build(uri), protocolVersion, headers, body);
    }

    private HTTPRequest(RequestMethod method, HTTPAddress address, ProtocolVersion protocolVersion, HTTPHeaders headers, String body) {
        super(protocolVersion, headers);
        this.method = method;
        this.address = address;
        this.body = body;
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

    public String body() {
        return body;
    }

    public <T> T body(BodyReader<T> reader) {
        String contentType = headers.getMandatory("Content-Type", String.class);
        return null;
    }

    HTTPRequest ofRoute(Route<?> route) {
        return new HTTPRequest(method, address.ofRoute(route.uri), protocolVersion, headers, body);
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
