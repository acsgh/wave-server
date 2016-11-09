package com.acs.wave.provider.common;

import com.acs.wave.router.HTTPHeader;
import com.acs.wave.router.HTTPHeaders;
import com.acs.wave.router.HTTPRequest;
import com.acs.wave.router.HTTPResponse;
import com.acs.wave.router.constants.ProtocolVersion;
import com.acs.wave.router.constants.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;

public final class ServletUtils {
    private ServletUtils() {
    }


    public static void transferParams(HTTPResponse waveResponse, HttpServletResponse response) throws IOException {
        waveResponse.headers.stream().forEach(header -> response.addHeader(header.key, header.value));
        response.getOutputStream().write(waveResponse.body);
        response.setStatus(waveResponse.responseStatus.code);
    }

    public static HTTPRequest toWaveRequest(HttpServletRequest request) {
        return new HTTPRequest(
                getMethod(request.getMethod()),
                getUri(request),
                getProtocol(request.getProtocol()),
                getHeaders(request),
                request.getRemoteAddr(),
                getBody(request)
        );
    }

    private static String getUri(HttpServletRequest request) {
        String result = request.getRequestURI();

        if (request.getQueryString() != null) {
            result += "?" + request.getQueryString();
        }

        return result;
    }

    private static byte[] getBody(HttpServletRequest request) {
        return new byte[0];
    }

    private static HTTPHeaders getHeaders(HttpServletRequest request) {
        List<HTTPHeader> headers = new ArrayList<>();

        forAll(request.getHeaderNames(), name -> {
            String value = request.getHeader(name);

            if (value != null) {
                headers.add(new HTTPHeader(name, value));
            }
        });

        return new HTTPHeaders(headers);
    }

    private static <T> void forAll(Enumeration<T> values, Consumer<T> action) {
        while (values.hasMoreElements()) {
            action.accept(values.nextElement());
        }
    }

    private static ProtocolVersion getProtocol(String protocol) {
        return ProtocolVersion.fromString(protocol);
    }

    private static RequestMethod getMethod(String method) {
        return RequestMethod.fromString(method);
    }
}
