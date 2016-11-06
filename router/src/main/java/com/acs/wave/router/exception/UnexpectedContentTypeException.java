package com.acs.wave.router.exception;

public class UnexpectedContentTypeException extends ParameterException {

    public UnexpectedContentTypeException(String contentType) {
        super("Unexpected content type '" + contentType + "'");
    }
}
