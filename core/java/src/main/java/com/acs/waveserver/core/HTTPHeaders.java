package com.acs.waveserver.core;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HTTPHeaders {
    private final List<HTTPHeader> headers;

    public HTTPHeaders() {
        this(new ArrayList<>());
    }

    public HTTPHeaders(List<HTTPHeader> headers) {
        this.headers = new ArrayList<>(headers);
    }

    public void add(String key, Object value) {
        headers.add(new HTTPHeader(key, toHeaderString(value)));
    }

    public Stream<HTTPHeader> stream() {
        return headers.stream();
    }

    public HTTPHeaders clone() {
        return new HTTPHeaders(headers.stream().map(HTTPHeader::clone).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "HTTPHeaders{" +
                "headers='" + headers + '\'' +
                '}';
    }

    private String toHeaderString(Object value) {
        if (value instanceof Date) {
            return dateToString((Date) value);
        } else if (value instanceof Calendar) {
            return dateToString(((Calendar) value).getTime());
        } else {
            return value.toString();
        }
    }

    private String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }
}
