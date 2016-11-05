package com.acs.waveserver.core.files;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.function.Supplier;

public class StaticClasspathFolderFilter extends FileFilter {

    private final String baseFolder;

    public StaticClasspathFolderFilter(String baseFolder, boolean cache) throws FileNotFoundException {
        super(cache);

        this.baseFolder = removeTradingSlash(removeEndingSlash(addTradingSlash(baseFolder)));
    }

    public Optional<FileInfo> getFileInfo(String fileName) {
        String file = baseFolder +  addTradingSlash(fileName);
log.info("Rear url: {}", file);
        URL url = Thread.currentThread().getContextClassLoader().getResource(file);

        Optional<FileInfo> result = Optional.empty();
        if ((url != null) && (!file.endsWith("/"))) {
            Supplier<byte[]> contentSupplier = () -> loadFileContent(file);
            result = Optional.of(new FileInfo(getContentType(fileName), getEtag(contentSupplier.get()), null, contentSupplier));
        }
        return result;
    }

    private byte[] loadFileContent(String file) {
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(file)) {
            return getBytes(input);
        } catch (Exception e) {
            log.info("Unable to read file", e);
            return new byte[0];
        }
    }
}
