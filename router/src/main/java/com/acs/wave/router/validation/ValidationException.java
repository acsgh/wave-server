package com.acs.wave.router.validation;

import com.acs.wave.router.exception.ParameterException;

public class ValidationException extends ParameterException {
    public ValidationException(String msg) {
        super(msg);
    }
}
