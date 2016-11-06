package com.acs.waveserver.core;

import com.acs.waveserver.core.exception.ParameterNotFoundException;
import com.acs.waveserver.utils.ValuesConverter;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HTTPParams {
    private final Map<String, String> params;

    public HTTPParams() {
        this(new HashMap<>());
    }

    public HTTPParams(Map<String, String> params) {
        this.params = Collections.unmodifiableMap(params);
    }

    public Set<String> keySet() {
        return params.keySet();
    }

    public void forEach(BiConsumer<? super String, ? super String> action) {
        params.forEach(action);
    }

    public <T> T getMandatory(String key, Class<T> parameterClass) {
        return get(key, parameterClass).orElseThrow(() -> new ParameterNotFoundException(key));
    }

    public <T> T getMandatory(String key, Function<String, T> converter) {
        return get(key, converter).orElseThrow(() -> new ParameterNotFoundException(key));
    }

    public <T> T getOrDefault(String key, Class<T> parameterClass, T defaultValue) {
        return get(key, parameterClass).orElse(defaultValue);
    }

    public <T> T getOrDefault(String key, Function<String, T> converter, T defaultValue) {
        return get(key, converter).orElse(defaultValue);
    }

    public <T> Optional<T> get(String key, Class<T> parameterClass) {
        return get(key, value -> ValuesConverter.convert(key, value, parameterClass));
    }

    public <T> Optional<T> get(String key, Function<String, T> converter) {
        return Optional.ofNullable(params.get(key)).map(converter);
    }

    @Override
    public String toString() {
        return "HTTPParams{" +
                "params='" + params + '\'' +
                '}';
    }
}
