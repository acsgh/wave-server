package com.acs.waveserver.core.files;

import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;
import com.acs.waveserver.core.HTTPResponseBuilder;
import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.core.functional.RequestFilter;
import com.acs.waveserver.utils.cache.CacheMap;
import com.acs.waveserver.utils.cache.CacheMapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

abstract class FileFilter implements RequestFilter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    private final boolean cache;

    private final CacheMap<String, FileInfo> fileInfoCache = new CacheMapBuilder<String, FileInfo>()
            .withTimeout(1L, TimeUnit.MINUTES)
            .withProvider(key -> getFileInfo(key).orElse(null))
            .build();

    protected FileFilter(boolean cache) {
        this.cache = cache;
    }

    @Override
    public Optional<HTTPResponse> handle(HTTPRequest request, HTTPResponseBuilder responseBuilder, Supplier<Optional<HTTPResponse>> nextJump) {
        String uri = getUri(request);
        log.debug("Requesting file: {}", uri);
        FileInfo fileInfo = fileInfoCache.get(uri);

        if (fileInfo != null) {
            responseBuilder.header("Content-Type", fileInfo.contentType);

            if (fileInfo.etag != null) {
                responseBuilder.header("ETag", fileInfo.etag);
            }

            if (fileInfo.lastModified != null) {
                responseBuilder.header("Last-Modified", DATE_FORMATTER.format(fileInfo.lastModified));
            }

            if ((!cache) || fileInfo.isModified(request.headers)) {
                responseBuilder.body(fileInfo.content());
                return responseBuilder.buildOption();
            } else {
                return responseBuilder.errorOption(ResponseStatus.NOT_MODIFIED);
            }

        }
        return Optional.empty();
    }

    private String getUri(HTTPRequest request) {
        return request.uri();
    }

    protected abstract Optional<FileInfo> getFileInfo(String uri);


    protected String getEtag(byte[] bytes) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
            BigInteger bigInt = new BigInteger(1, digest);

            result = bigInt.toString(16).toUpperCase();
            while (result.length() < 32) {
                result = "0" + result;
            }
        } catch (NoSuchAlgorithmException e) {
            log.debug("Unable to create MD5", e);
        }
        return result;
    }

//    private boolean serverFreshContent(HTTPRequest request, Date lastModifiedFile) {
//        Optional<String> lastModifiedHeader = request.headers.getSingle("If-Modified-Since", String.class);
//
//        boolean result = true;
//
//        if (lastModifiedHeader.isPresent()) {
//            try {
//                Date lastModifiedClient = DATE_FORMATTER.parse(lastModifiedHeader.get());
//                result = lastModifiedFile.after(lastModifiedClient);
//            } catch (Exception e) {
//                log.trace("Unable to parse last modified exception", e);
//            }
//        }
//
//        return result;
//    }

    protected String getContentType(String filename) {
        String result = URLConnection.guessContentTypeFromName(filename);

        if (filename.endsWith(".js")) {
            result = "application/javascript";
        } else if (filename.endsWith(".css")) {
            result = "text/css";
        }

        return result;
    }
}
