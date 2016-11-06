package com.acs.wave.router.files;

import com.acs.wave.router.HTTPHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

class FileInfo {
    private final Logger log = LoggerFactory.getLogger(getClass());

    final String contentType;
    final String etag;
    final Date lastModified;
    private final Supplier<byte[]> contentSupplier;

    FileInfo(String contentType, String etag, Date lastModified, Supplier<byte[]> contentSupplier) {
        this.contentType = contentType;
        this.etag = etag;
        this.lastModified = lastModified;
        this.contentSupplier = contentSupplier;
    }

    boolean isModified(HTTPHeaders headers) {
        boolean result;

        Optional<String> expectedEtag = headers.getSingle("If-None-Match", String.class);
        if (expectedEtag.isPresent() && (etag != null)) {
            result = !etag.equalsIgnoreCase(expectedEtag.get());
        } else {
            result = true;
        }

        Optional<String> expectedDate = headers.getSingle("If-Modified-Since", String.class);
        if (result) {
            if (expectedDate.isPresent() && (lastModified != null)) {
                try {
                    Date lastModifiedClient = FileFilter.DATE_FORMATTER.parse(expectedDate.get());
                    result = lastModified.after(lastModifiedClient);
                } catch (ParseException e) {
                    log.debug("Unable to parse date", e);
                    result = true;
                }
            } else {
                result = true;
            }
        }

        return result;
    }

    byte[] content() {
        return contentSupplier.get();
    }
}
