package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.RequestMethod;

import java.util.List;
import java.util.Map;

public class HTTPRequest extends HTTPItem{
    public final RequestMethod method;
    public final String uri;

    public HTTPRequest(RequestMethod method, String uri, ProtocolVersion protocolVersion, HTTPHeaders headers) {
        super(protocolVersion, headers);
        this.method = method;
        this.uri = uri;
    }

    public HTTPRequest withParams(Map<String, Object> params) {
        return this;
    }

    HTTPRequest ofRoute(Route<?> route){
        return withParams(route.extractParams(uri));
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
