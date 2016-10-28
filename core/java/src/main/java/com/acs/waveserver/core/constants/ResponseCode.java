package com.acs.waveserver.core.constants;

public enum ResponseCode {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    public final int code;
    public final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
