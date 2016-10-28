package com.acs.waveserver.core.utils;


public final class CheckUtils {
    private CheckUtils() {

    }

    public static void checkNull(String name, Object value) {
        if (value == null) {
            throw new NullPointerException("The '" + name + "' is null");
        }
    }

    public static void checkStringEmpty(String name, String value) {
        checkStringLength(name, value, 0, -1);
    }

    public static void checkStringLength(String name, String value, int min, int max) {
        checkNull(name, value);

        if ((min > -1) && (value.length() < min)) {
            throw new IllegalArgumentException("The field '" + name + "' has to be bigger than " + min);
        }

        if ((max > -1) && (value.length() > max)) {
            throw new IllegalArgumentException("The field '" + name + "' has to be smaller than " + max);
        }
    }
}
