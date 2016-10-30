package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.core.utils.ExceptionUtils;

public class HTTPResponse extends HTTPItem{
    public final ResponseStatus responseStatus;
    public final Object body;

    HTTPResponse(ProtocolVersion protocolVersion, ResponseStatus responseStatus, HTTPHeaders headers, Object body) {
        super(protocolVersion, headers);
        this.responseStatus = responseStatus;
        this.body = body;
    }

    @Override
    public String toString() {
        return "HTTPResponse{" +
                "protocolVersion=" + protocolVersion +
                ", responseStatus=" + responseStatus +
                ", headers=" + headers +
                ", body=" + body.getClass().getName() +
                '}';
    }

    public byte[] getBytes() {
        byte[] result = new byte[0];

        if (body != null) {
            if (body instanceof String) {
                result = stringToBytes((String)  body);
            }
        }

        return result;
    }

    public byte[] stringToBytes(String string) {
        byte[] result = null;
        try {
            result= string. getBytes("UTF-8");
        } catch (Exception e) {
            ExceptionUtils.throwRuntimeException(e);
        }
        return result;
    }
}

