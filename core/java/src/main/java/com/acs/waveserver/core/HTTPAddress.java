package com.acs.waveserver.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HTTPAddress {

    public static HTTPAddress build(String rawUri) {
        try {
            URI uri = new URI(rawUri);
            return new HTTPAddress(uri, new HashMap<>(), new HashMap<>());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private final URI uri;
    private final Map<String, Object> pathParams;
    private final Map<String, Object> queryParams;

    public HTTPAddress(URI uri, Map<String, Object> pathParams, Map<String, Object> queryParams) {
        this.uri = uri;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
    }

    public String getScheme() {
        return uri.getScheme();
    }

    public boolean isAbsolute() {
        return uri.isAbsolute();
    }

    public String getHost() {
        return uri.getHost();
    }

    public int getPort() {
        return uri.getPort();
    }

    public String getRawPath() {
        return uri.getRawPath();
    }

    public String getPath() {
        return uri.getPath();
    }

    public String getRawQuery() {
        return uri.getRawQuery();
    }

    public String getQuery() {
        return uri.getQuery();
    }

    public HTTPAddress clone() {
        return new HTTPAddress(uri, pathParams, queryParams);
    }

    HTTPAddress ofRoute(Route<?> route) {
        return this;
    }

    @Override
    public String toString() {
        return "HTTPAddress{" +
                "uri='" + uri + '\'' +
                ", pathParams=" + pathParams +
                ", queryParams=" + queryParams +
                '}';
    }
}
