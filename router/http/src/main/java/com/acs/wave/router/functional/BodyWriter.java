package com.acs.wave.router.functional;

@FunctionalInterface
public interface BodyWriter {
    default String contentType() {
        return "text/html";
    }

    <T> byte[] write(T body);

}
