package com.acs.waveserver.core.constants;

public enum RequestMethod {
    OPTIONS, GET, HEAD, POST, PUT, PATCH, DELETE, TRACE, CONNECT;

    public static RequestMethod fromString(String name) {
        for (RequestMethod method : values()) {
            if (method.toString().equals(name)) {
                return method;
            }
        }
        throw new IllegalArgumentException("There is no method with name: " + name);
    }
}