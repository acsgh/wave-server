package com.acs.waveserver.core.files;

import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;
import com.acs.waveserver.core.HTTPResponseBuilder;
import com.acs.waveserver.core.functional.RequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Optional;
import java.util.function.Supplier;

public class WebJarsHandler implements RequestFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ClassLoader classLoader;

    public WebJarsHandler(ClassLoader classLoader) {
        this.classLoader = classLoader;

    }

//    @Override
//    public void configure(ZooService service) {
//        service.get("/webjars/*", (request, Response) -> handleWebjars(service, request, Response));
//    }

    @Override
    public Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, Supplier<Optional<HTTPResponse>> nextJump) {
        String baseUrl = getBaseUrl(request);
        String resourceUrl = "META-INF/resources/webjars" + baseUrl;
        try (InputStream input = classLoader.getResourceAsStream(resourceUrl)) {
            if (input != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                IOUtils.copy(input, outputStream);
                String filename = baseUrl.substring(baseUrl.lastIndexOf("/") + 1);
                byte[] result = outputStream.toByteArray();

                if (filename.endsWith(".js")) {
                    responseBuilder.header("Content-Type", "application/javascript");
                } else if (filename.endsWith(".css")) {
                    responseBuilder.header("Content-Type", "text/css");
                } else {
                    responseBuilder.header("Content-Type", URLConnection.guessContentTypeFromName(filename));
                }
            } else {
                return nextJump.get();
            }
        } catch (IOException e) {
            log.error("Error serving webjars: " + baseUrl, e);
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private String getBaseUrl(HTTPRequest request) {
        return request.uri().replace("/webjars", "");
    }
}
