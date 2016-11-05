package com.acs.waveserver.converter.json;

import com.acs.waveserver.core.exception.ParameterException;
import com.acs.waveserver.core.functional.BodyReader;
import com.acs.waveserver.core.utils.ExceptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Set;

public class JsonBodyReader<T> implements BodyReader<T> {

    private final ObjectMapper objectMapper;
    private final Class<T> objectClass;

    public JsonBodyReader(ObjectMapper objectMapper, Class<T> objectClass) {
        this.objectMapper = objectMapper;
        this.objectClass = objectClass;
    }

    @Override
    public Set<String> contentType() {
        return toSet("application/json");
    }

    @Override
    public T read(byte[] body) {
        T result = null;
        try {
            result = objectMapper.readValue(body, objectClass);
        } catch (JsonProcessingException e) {
            throw new ParameterException("Unable to parse JSON", e);
        } catch (IOException e) {
            ExceptionUtils.throwRuntimeException(e);
        }
        return result;
    }
}