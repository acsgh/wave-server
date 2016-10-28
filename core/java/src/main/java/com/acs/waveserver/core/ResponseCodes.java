package com.acs.waveserver.core;

public enum ResponseCodes {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NOT_FOUND(404, "Not Found");

    public final int code;
    public final String message;

    ResponseCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
