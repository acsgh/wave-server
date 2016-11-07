package com.acs.wave.router.functional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@FunctionalInterface
public interface BodyReader<T> {

    default Set<String> contentType() {
        return new HashSet<>();
    }

    default Set<String> toSet(String... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    T read(byte[] body);

}
