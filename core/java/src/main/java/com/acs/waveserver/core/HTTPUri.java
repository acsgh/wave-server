package com.acs.waveserver.core;

import java.util.HashMap;
import java.util.Map;

public class HTTPUri {

    public static HTTPUri build(String uri){
        return new HTTPUri(uri, new HashMap<>(), new HashMap<>());
    }
    public final String rawUri;
    private final Map<String, Object> pathParams;
    private final Map<String, Object> queryParams;

    public HTTPUri(String rawUri, Map<String, Object> pathParams, Map<String, Object> queryParams) {
        this.rawUri = rawUri;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
    }

    public HTTPUri clone(){
        return new HTTPUri(rawUri, pathParams, queryParams);
    }

    HTTPUri ofRoute(Route<?> route) {
        return this;
    }

    @Override
    public String toString() {
        return "HTTPUri{" +
                "rawUri='" + rawUri + '\'' +
                ", pathParams=" + pathParams +
                ", queryParams=" + queryParams +
                '}';
    }
}
