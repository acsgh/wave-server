package com.acs.waveserver.core;

import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.RequestMethod;
import com.acs.waveserver.core.exception.UnexpectedContentTypeException;
import com.acs.waveserver.core.functional.BodyReader;
import com.acs.waveserver.core.utils.ExceptionUtils;

import java.util.Set;

public class HTTPRequest extends HTTPItem {

    public final RequestMethod method;
    final HTTPAddress address;
    private final byte[] body;

    public HTTPRequest(RequestMethod method, String uri, ProtocolVersion protocolVersion, HTTPHeaders headers, byte[] body) {
        this(method, HTTPAddress.build(uri), protocolVersion, headers, body);
    }

    private HTTPRequest(RequestMethod method, HTTPAddress address, ProtocolVersion protocolVersion, HTTPHeaders headers, byte[] body) {
        super(protocolVersion, headers);
        this.method = method;
        this.address = address;
        this.body = body;
    }

    public String uri() {
        return address.uri.getPath();
    }

    public String fullUri() {
        return address.uri.toString();
    }

    public HTTPParams pathParams() {
        return address.pathParams;
    }

    public HTTPParams queryParams() {
        return address.queryParams;
    }

    public byte[] body() {
        return body;
    }

    public String bodyAsString() {
        return bytesToString(body);
    }

    public <T> T body(BodyReader<T> reader) {
        String contentType = headers.getMandatory("Content-Type", String.class);

        if (!validContentType(reader.contentType(), contentType)) {
            throw new UnexpectedContentTypeException(contentType);
        }
        return reader.read(body());
    }

    private boolean validContentType(Set<String> contentTypes, String contentType) {
        return (contentTypes.isEmpty()) || (contentTypes.stream().anyMatch(type -> type.equalsIgnoreCase(contentType)));
    }

    HTTPRequest ofRoute(Route<?> route) {
        return new HTTPRequest(method, address.ofRoute(route.uri), protocolVersion, headers, body);
    }

    @Override
    public String toString() {
        return "HTTPRequest{" +
                "methods=" + method +
                ", uri='" + uri() + '\'' +
                ", protocolVersion=" + protocolVersion +
                ", headers=" + headers +
                ", pathParams='" + pathParams() + '\'' +
                ", queryParams='" + queryParams() + '\'' +
                '}';
    }

    private String bytesToString(byte[] bytes) {
        String result = null;
        try {
            result = new String(bytes, "UTF-8");
        } catch (Exception e) {
            ExceptionUtils.throwRuntimeException(e);
        }
        return result;
    }
}
