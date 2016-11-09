package com.acs.wave.router.functional;

@FunctionalInterface
public interface BodyWriter<T> {
    default String contentType() {
        return "text/html";
    }

    byte[] write(T body);

}
