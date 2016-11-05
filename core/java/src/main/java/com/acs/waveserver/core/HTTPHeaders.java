package com.acs.waveserver.core;

import com.acs.waveserver.core.exception.ParameterNotFoundException;
import com.acs.waveserver.utils.ValuesConverter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
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

    public Set<String> keys() {
        return headers.stream()
                .map(header -> header.key)
                .collect(Collectors.toSet());
    }

    public boolean containsKey(String key) {
        return headers.stream().anyMatch(header -> header.key.equalsIgnoreCase(key));
    }

    public <T> T getMandatory(String key, Class<T> parameterClass) {
        return getSingle(key, parameterClass).orElseThrow(() -> new ParameterNotFoundException("Header", key));
    }

    public <T> T getMandatory(String key, Function<String, T> converter) {
        return getSingle(key, converter).orElseThrow(() -> new ParameterNotFoundException("Header", key));
    }

    public <T> T getOrDefault(String key, Class<T> parameterClass, T defaultValue) {
        return getSingle(key, parameterClass).orElse(defaultValue);
    }

    public <T> T getOrDefault(String key, Function<String, T> converter, T defaultValue) {
        return getSingle(key, converter).orElse(defaultValue);
    }

    public <T> Optional<T> getSingle(String key, Class<T> parameterClass) {
        return get(key, parameterClass).stream().findFirst();
    }

    public <T> Optional<T> getSingle(String key, Function<String, T> converter) {
        return get(key, converter).stream().findFirst();
    }

    public <T> List<T> get(String key, Class<T> parameterClass) {
        return get(key, value -> ValuesConverter.convert(key, value, parameterClass));
    }

    public <T> List<T> get(String key, Function<String, T> converter) {
        return headers.stream()
                .filter(header -> header.key.equalsIgnoreCase(key))
                .map(header -> converter.apply(header.value))
                .collect(Collectors.toList());
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
