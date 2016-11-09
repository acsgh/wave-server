package com.acs.wave.router;

import com.acs.wave.router.constants.ProtocolVersion;
import com.acs.wave.router.constants.RequestMethod;
import com.acs.wave.router.exception.UnexpectedContentTypeException;
import com.acs.wave.router.functional.BodyReader;
import com.acs.wave.utils.ExceptionUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

public class HTTPRequest extends HTTPItem {

    public final RequestMethod method;
    final HTTPAddress address;
    public final String remoteAddress;
    private final byte[] body;

    public HTTPRequest(RequestMethod method, String uri, ProtocolVersion protocolVersion, HTTPHeaders headers, String remoteAddress, byte[] body) {
        this(method, HTTPAddress.build(uri), protocolVersion, headers, remoteAddress, body);
    }

    private HTTPRequest(RequestMethod method, HTTPAddress address, ProtocolVersion protocolVersion, HTTPHeaders headers, String remoteAddress, byte[] body) {
        super(protocolVersion, headers);
        this.method = method;
        this.address = address;
        this.remoteAddress = remoteAddress;
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


    HTTPRequest ofUri(String uri) {
        return new HTTPRequest(method, HTTPAddress.build(uri), protocolVersion, headers, remoteAddress, body);
    }

    HTTPRequest ofRoute(Route<?> route) {
        return new HTTPRequest(method, address.ofRoute(route.uri), protocolVersion, headers, remoteAddress, body);
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
