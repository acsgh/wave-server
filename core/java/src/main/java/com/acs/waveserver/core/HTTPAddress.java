package com.acs.waveserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class HTTPAddress {

    private static final Logger log = LoggerFactory.getLogger(HTTPAddress.class);

    public static HTTPAddress build(String rawUri) {
        try {
            URI uri = new URI(rawUri);
            return new HTTPAddress(uri, new HTTPParams(), extractQueryParam(uri.getQuery()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static HTTPParams extractQueryParam(String query) {
        Map<String, String> params = Arrays.stream(query.split("&"))
                .map(HTTPAddress::toMapEntry)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new HTTPParams(params);
    }

    private static Optional<Map.Entry<String, String>> toMapEntry(String rawEntry) {
        Optional<Map.Entry<String, String>> result = Optional.empty();

        try {
            String[] parts = rawEntry.split("=");

            String key = parts[0];
            String value = (parts.length > 1) ? parts[1] : "";
            result = Optional.of(new AbstractMap.SimpleEntry<>(URLDecoder.decode(key, "UTF-8"), URLDecoder.decode(value, "UTF-8")));
        } catch (Exception e) {
            log.error("Unable to parse query params", e);
        }

        return result;
    }

    private final URI uri;
    private final HTTPParams pathParams;
    private final HTTPParams queryParams;

    public HTTPAddress(URI uri, HTTPParams pathParams, HTTPParams queryParams) {
        this.uri = uri;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
    }

    public URI getUri() {
        return uri;
    }

    public HTTPParams getPathParams() {
        return pathParams;
    }

    public HTTPParams getQueryParams() {
        return queryParams;
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
        return new HTTPAddress(uri, extractPathParams(route.uri), queryParams);
    }

    private HTTPParams extractPathParams(String routeUri) {
        Map<String, String> params = new HashMap<>();
        return new HTTPParams(params);
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
