package com.acs.waveserver.converter.json;

import com.acs.waveserver.core.functional.BodyConverter;
import com.acs.waveserver.core.utils.ExceptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonConverter implements BodyConverter {


    private static ObjectMapper getDefaultMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    private final ObjectMapper objectMapper;

    public JsonConverter() {
        this(getDefaultMapper());
    }

    public JsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public <T> String convert(T body) {
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
