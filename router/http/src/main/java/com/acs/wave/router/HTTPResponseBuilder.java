package com.acs.wave.router;

import com.acs.wave.router.constants.ProtocolVersion;
import com.acs.wave.router.constants.RedirectStatus;
import com.acs.wave.router.functional.BodyWriter;
import com.acs.wave.router.constants.ResponseStatus;
import com.acs.wave.utils.ExceptionUtils;

import java.util.Optional;

import static com.acs.wave.router.constants.ResponseStatus.OK;

public class HTTPResponseBuilder {

    private ProtocolVersion protocolVersion;
    private ResponseStatus responseStatus;
    private HTTPHeaders headers;
    private byte[] body;

    private final HTTPRequest request;
    private final HTTPRouter httpRouter;

    HTTPResponseBuilder(HTTPRequest request, HTTPRouter httpRouter) {
        this(request, httpRouter, new HTTPHeaders());
    }

    HTTPResponseBuilder(HTTPRequest request, HTTPRouter httpRouter, HTTPHeaders headers) {
        this.request = request;
        this.httpRouter = httpRouter;
        this.headers = headers;

        version(request.protocolVersion);
        status(OK);
    }

    public HTTPResponse build() {
        return new HTTPResponse(protocolVersion, responseStatus, headers, body);
    }

    public Optional<HTTPResponse> buildOption() {
        return Optional.of(build());
    }

    public HTTPResponse redirect(String url) {
        return redirect(url, RedirectStatus.FOUND);
    }

    public Optional<HTTPResponse> redirectOption(String url) {
        return redirectOption(url, RedirectStatus.FOUND);
    }

    public HTTPResponse redirect(String url, RedirectStatus redirectStatus) {
        HTTPResponseBuilder responseBuilder = clone();
        responseBuilder.header("Location", url);
        return httpRouter.getErrorResponse(request, responseBuilder, redirectStatus.status);
    }

    public Optional<HTTPResponse> redirectOption(String url, RedirectStatus status) {
        return Optional.of(redirect(url, status));
    }

    public HTTPResponse error(ResponseStatus errorCode) {
        return httpRouter.getErrorResponse(request, this, errorCode);
    }

    public Optional<HTTPResponse> errorOption(ResponseStatus errorCode) {
        return Optional.of(error(errorCode));
    }

    public HTTPResponse serve(String url) {
        return httpRouter.process(request.ofUri(url));
    }

    public Optional<HTTPResponse> serveOption(String url) {
        return Optional.of(serve(url));
    }

    public HTTPResponseBuilder header(String key, Object value) {
        headers.add(key, value);
        return this;
    }

    public HTTPResponseBuilder version(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    public HTTPResponseBuilder status(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        return this;
    }

    public HTTPResponseBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public HTTPResponseBuilder body(String body) {
        this.body = stringToBytes(body);
        return this;
    }


    public <T> HTTPResponseBuilder body(T body, BodyWriter<T> converter) {
        this.body = converter.write(body);

        if (!headers.containsKey("Content-Type")) {
            header("Content-Type", converter.contentType());
        }

        return this;
    }

    public HTTPResponseBuilder clone() {
        return new HTTPResponseBuilder(request, httpRouter, headers.clone());
    }

    private byte[] stringToBytes(String string) {
        byte[] result = null;
        try {
            result = string.getBytes("UTF-8");
        } catch (Exception e) {
            ExceptionUtils.throwRuntimeException(e);
        }
        return result;
    }
}
