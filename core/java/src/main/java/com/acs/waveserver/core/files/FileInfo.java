package com.acs.waveserver.core.files;

import com.acs.waveserver.core.HTTPHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

public class FileInfo {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public final String contentType;
    public final String etag;
    public final Date lastModified;
    private final Supplier<byte[]> contentSupplier;

    public FileInfo(String contentType, String etag, Date lastModified, Supplier<byte[]> contentSupplier) {
        this.contentType = contentType;
        this.etag = etag;
        this.lastModified = lastModified;
        this.contentSupplier = contentSupplier;
    }

    public boolean isModified(HTTPHeaders headers) {
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

    public byte[] content() {
        return contentSupplier.get();
    }


}
