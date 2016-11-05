package com.acs.waveserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HTTPAddress {

    private static final Logger log = LoggerFactory.getLogger(HTTPAddress.class);

    static HTTPAddress build(String rawUri) {
        try {
            URI uri = new URI(rawUri);
            return new HTTPAddress(uri, new HTTPParams(), extractQueryParam(uri.getQuery()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static HTTPParams extractQueryParam(String query) {
        if (query == null) {
            return new HTTPParams();
        }
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

    final URI uri;
    final HTTPParams pathParams;
    final HTTPParams queryParams;

    HTTPAddress(URI uri, HTTPParams pathParams, HTTPParams queryParams) {
        this.uri = uri;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
    }

    public HTTPAddress clone() {
        return new HTTPAddress(uri, pathParams, queryParams);
    }

    HTTPAddress ofRoute(String routeUri) {
        return new HTTPAddress(uri, extractPathParams(routeUri), queryParams);
    }


    boolean matchUrl(String routeUri) {
        String pattern = getPattern(routeUri);
        return Pattern.matches(pattern, uri.getPath());
    }

    private HTTPParams extractPathParams(String routeUri) {
        Map<String, String> params = new HashMap<>();

        if (matchUrl(routeUri)) {
            List<String> names = getParamNames(routeUri);

            String patternString = getPattern(routeUri);

            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(uri.getPath());
            if (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    params.put(names.get(i - 1), urlDecode(matcher.group(i)));
                }
            }
        }

        return new HTTPParams(params);
    }

    private String urlDecode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPattern(String routeUri) {
        String pattern = routeUri.replaceAll("\\*", ".*");
        pattern = pattern.replaceAll("\\{[^/{}+]*\\}", "([^/{}]*)");
        pattern = pattern.replaceAll("\\{[^/{}]*\\+\\}", "([^{}]*)");
        return pattern;
    }

    private List<String> getParamNames(String routeUri) {
        List<String> names = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\{[^/{}]*\\})");
        Matcher matcher = pattern.matcher(routeUri);
        while (matcher.find()) {
            names.add(matcher.group().replace("{", "").replace("+}", "").replace("}", ""));
        }
        return names;
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
