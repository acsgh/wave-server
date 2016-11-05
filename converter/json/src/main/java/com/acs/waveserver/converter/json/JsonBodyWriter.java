package com.acs.waveserver.converter.json;

import com.acs.waveserver.core.functional.BodyWriter;
import com.acs.waveserver.utils.ExceptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;

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
    public <T> byte[] write(T body) {
        byte[] result = null;

        if (body != null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                objectMapper.writeValue(out, body);
                result = out.toByteArray();
            } catch (Exception e) {
                ExceptionUtils.throwRuntimeException(e);
            }
        }

        return result;
    }
}
