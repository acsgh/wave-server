package com.acs.wave.utils;


import com.acs.wave.utils.exception.InvalidParameterFormatException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ValuesConverter {
    private ValuesConverter() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(String key, String value, Class<T> parameterClass) {
        try {
            if (parameterClass == String.class) {
                return (T) value;
            } else if (isNumber(parameterClass)) {
                return (T) convertToNumber(value, (Class<? extends Number>) parameterClass);
            } else if (isBoolean(parameterClass)) {
                return (T) convertToBoolean(value);
            } else if (parameterClass.isEnum()) {
                return (T) convertToEnum(value, (Class<? extends Enum>) parameterClass);
            } else {
                throw new InvalidParameterFormatException(key, value, parameterClass);
            }
        } catch (InvalidParameterFormatException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidParameterFormatException(key, value, parameterClass, e);
        }
    }

    private static <T> boolean isBoolean(Class<T> parameterClass) {
        return Boolean.class.isAssignableFrom(parameterClass) || boolean.class.isAssignableFrom(parameterClass);
    }

    private static <T> boolean isNumber(Class<T> parameterClass) {
        return Number.class.isAssignableFrom(parameterClass) || (parameterClass.isPrimitive() || (parameterClass != boolean.class) || (parameterClass != char.class));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertToNumber(String value, Class<T> targetClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (byte.class == targetClass) {
            return (T) Byte.valueOf(value);
        } else if (short.class == targetClass) {
            return (T) Short.valueOf(value);
        } else if (int.class == targetClass) {
            return (T) Integer.valueOf(value);
        } else if (long.class == targetClass) {
            return (T) Long.valueOf(value);
        } else if (float.class == targetClass) {
            return (T) Float.valueOf(value);
        } else if (double.class == targetClass) {
            return (T) Double.valueOf(value);
        } else {
            Constructor<T> constructor = targetClass.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(value);
        }
    }

    public static <T extends Enum<T>> T convertToEnum(String value, Class<T> targetClass) {
        return Enum.valueOf(targetClass, value);
    }

    public static Boolean convertToBoolean(String value) {
        if ("0".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return false;
        } else if ("1".equals(value) || "true".equalsIgnoreCase(value)) {
            return true;
        } else {
            throw new IllegalArgumentException("Value '" + "' cannot be converted to boolean");
        }
    }
}
