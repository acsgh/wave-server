package com.acs.waveserver.examples;

public class JsonSupport {
    public <T> String marshall(T object) {
        return "{}";
    }

    public <T> T unmarshall(String input, Class<T> targetClass) {
        return null;
    }
}
