package com.acs.waveserver.core.validation;

public interface Validation {
    void validate() throws ValidationException;

    default void checkNotNull(String name, Object field) throws ValidationException {
        if (field == null) {
            throw new ValidationException("Field '" + name + "' cannot be null");
        }
    }
}
