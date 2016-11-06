package com.acs.waveserver.core.validation;

import com.acs.waveserver.core.exception.ParameterException;

public class ValidationException extends ParameterException {
    public ValidationException(String msg) {
        super(msg);
    }
}
