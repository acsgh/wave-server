package com.acs.waveserver.core.functional;

@FunctionalInterface
public interface BodyConverter {
    default String contentType() {
        return "text/html";
    }

    <T> String convert(T body);
}
