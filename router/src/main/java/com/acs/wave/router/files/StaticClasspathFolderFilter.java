package com.acs.wave.router.files;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.function.Supplier;

public class StaticClasspathFolderFilter extends FileFilter {

    private final String baseFolder;
    private final ClassLoader classLoader;

    public StaticClasspathFolderFilter(String baseFolder, boolean cache) {
        this(baseFolder, cache, Thread.currentThread().getContextClassLoader());
    }

    public StaticClasspathFolderFilter(String baseFolder, boolean cache, ClassLoader classLoader) {
        super(cache);
        this.baseFolder = removeTradingSlash(removeEndingSlash(addTradingSlash(baseFolder)));
        this.classLoader = classLoader;
    }

    public Optional<FileInfo> getFileInfo(String fileName) {
        String file = baseFolder + addTradingSlash(fileName);

        URL url = classLoader.getResource(file);

        Optional<FileInfo> result = Optional.empty();
        if ((url != null) && (!file.endsWith("/"))) {
            Supplier<byte[]> contentSupplier = () -> loadFileContent(file);
            result = Optional.of(new FileInfo(getContentType(fileName), getEtag(contentSupplier.get()), null, contentSupplier));
        }
        return result;
    }

    private byte[] loadFileContent(String file) {
        try (InputStream input = classLoader.getResourceAsStream(file)) {
            return getBytes(input);
        } catch (Exception e) {
            log.info("Unable to read file", e);
            return new byte[0];
        }
    }
}
