package com.acs.waveserver.converter.json;

import com.acs.waveserver.core.functional.BodyWriter;
import com.acs.waveserver.core.utils.ExceptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonBodyWriter implements BodyWriter {

    private final ObjectMapper objectMapper;

    public JsonBodyWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public <T> String write(T body) {
        String result = null;

        if (body != null) {
            try {
                result = objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                ExceptionUtils.throwRuntimeException(e);
            }
        }

        return result;
    }
}
