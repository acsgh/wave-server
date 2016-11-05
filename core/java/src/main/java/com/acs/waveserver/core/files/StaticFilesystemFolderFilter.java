package com.acs.waveserver.core.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

public class StaticFilesystemFolderFilter extends FileFilter {

    private final File baseFolder;

    public StaticFilesystemFolderFilter(File baseFolder, boolean cache) throws FileNotFoundException {
        super(cache);

        this.baseFolder = baseFolder;

        if (!baseFolder.exists() || !baseFolder.isDirectory()) {
            throw new FileNotFoundException("Folder " + baseFolder.getAbsolutePath() + " does not exist");
        }
    }

    public Optional<FileInfo> getFileInfo(String fileName) {
        File file = new File(baseFolder, fileName);

        Optional<FileInfo> result = Optional.empty();
        if (file.exists() && !file.isDirectory()) {
            Supplier<byte[]> contentSupplier = () -> loadFileContent(file);
            result = Optional.of(new FileInfo(getContentType(fileName), getEtag(contentSupplier.get()), getLastModified(file), contentSupplier));
        }
        return result;
    }

    private byte[] loadFileContent(File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            return getBytes(input);
        } catch (Exception e) {
            log.info("Unable to read file", e);
            return new byte[0];
        }
    }

    private Date getLastModified(File file) {
        return Date.from(Instant.ofEpochMilli(file.lastModified()));
    }
}
