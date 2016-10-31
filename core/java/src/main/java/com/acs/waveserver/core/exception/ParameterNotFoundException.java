package com.acs.waveserver.core.exception;

public class ParameterNotFoundException extends ParameterException {
    public ParameterNotFoundException(String parameterKey) {
        super("Parameter '" + parameterKey + "' not found");
    }
}
