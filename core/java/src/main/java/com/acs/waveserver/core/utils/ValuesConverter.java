package com.acs.waveserver.core.utils;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ValuesConverter {
    private ValuesConverter() {

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
