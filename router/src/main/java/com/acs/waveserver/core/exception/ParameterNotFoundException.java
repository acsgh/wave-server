package com.acs.waveserver.core.exception;

public class ParameterNotFoundException extends ParameterException {
    public ParameterNotFoundException(String parameterKey) {
        this("Parameter", parameterKey);
    }

    public ParameterNotFoundException(String parameterType, String parameterKey) {
        super(parameterType + " '" + parameterKey + "' not found");
    }
}
