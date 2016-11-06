package com.acs.waveserver.core;

public class HTTPHeader {
    public final String key;
    public final String value;

    public HTTPHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public HTTPHeader clone() {
        return new HTTPHeader(key, value);
    }

    @Override
    public String toString() {
        return "HTTPHeader{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
