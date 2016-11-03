package com.acs.waveserver.core.functional;

@FunctionalInterface
public interface BodyWriter {
    default String contentType() {
        return "text/html";
    }

    <T> String write(T body);

}
