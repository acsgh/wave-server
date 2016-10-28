package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.HTTPVersion;
import com.acs.waveserver.core.constants.RequestMethod;

import java.net.URI;

public class HTTPRequest {
    public final RequestMethod method;
    public final URI uri;
    public final HTTPVersion httpVersion;

    public HTTPRequest(RequestMethod method, URI uri, HTTPVersion httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }
}
