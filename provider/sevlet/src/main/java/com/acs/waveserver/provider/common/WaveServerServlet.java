package com.acs.waveserver.provider.common;

import com.acs.waveserver.core.*;
import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;

public class WaveServerServlet extends HttpServlet {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final Router router;

    public WaveServerServlet(Router router) {
        this.router = router;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HTTPResponse waveResponse = router.process(toWaveRequest(request));
        transferParams(waveResponse, response);
    }

    private void transferParams(HTTPResponse waveResponse, HttpServletResponse response) throws IOException {
        waveResponse.headers.stream().forEach(header -> response.addHeader(header.key, header.value));
        response.getOutputStream().write(waveResponse.body);
        response.setStatus(waveResponse.responseStatus.code);
    }

    private HTTPRequest toWaveRequest(HttpServletRequest request) {
        return new HTTPRequest(
                getMethod(request.getMethod()),
                request.getRequestURI(),
                getProtocol(request.getProtocol()),
                getHeaders(request),
                getBody(request)
        );
    }

    private byte[] getBody(HttpServletRequest request) {
        return new byte[0];
    }

    private HTTPHeaders getHeaders(HttpServletRequest request) {
        List<HTTPHeader> headers = new ArrayList<>();

        forAll(request.getHeaderNames(), name -> {
            String value = request.getHeader(name);

            if(value != null){
                headers.add(new HTTPHeader(name, value));
            }
        });

        return new HTTPHeaders(headers);
    }

    private <T> void forAll(Enumeration<T> values, Consumer<T> action){
        while(values.hasMoreElements()){
            action.accept(values.nextElement());
        }
    }

    private ProtocolVersion getProtocol(String protocol) {
        return ProtocolVersion.fromString(protocol);
    }

    private RequestMethod getMethod(String method) {
        return RequestMethod.fromString(method);
    }
}