package com.acs.waveserver.core.exception;

public class ParameterNotFoundException extends ParameterException {
    public ParameterNotFoundException(String parameterKey) {
        this("Parameter", parameterKey);
    }

    public ParameterNotFoundException(String parameterTpe, String parameterKey) {
        super(parameterTpe + " '" + parameterKey + "' not found");
    }
}
