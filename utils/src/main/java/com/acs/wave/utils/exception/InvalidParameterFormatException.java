package com.acs.wave.utils.exception;

public class InvalidParameterFormatException extends IllegalArgumentException {
    private static String getExceptionMessage(String key, String value, Class<?> parameterClass) {
        return "Parameter '" + key + "' with value '" + value + "' cannot be write to " + parameterClass.getName();
    }

    public <T> InvalidParameterFormatException(String key, String value, Class<T> parameterClass) {
        super(getExceptionMessage(key, value, parameterClass));
    }

    public <T> InvalidParameterFormatException(String key, String value, Class<T> parameterClass, Exception e) {
        super(getExceptionMessage(key, value, parameterClass), e);
    }
}
