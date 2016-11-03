package com.acs.waveserver.converter.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonBodyReaderProvider {

    private final ObjectMapper objectMapper;

    public JsonBodyReaderProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> JsonBodyReader<T> get(Class<T> bodyClass) {
        return new JsonBodyReader<>(objectMapper, bodyClass);
    }
}
