package com.acs.wave.converter.template;


import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;

public class FreemarkerEngineBuilder {

    private File templateFolder;
    private String classpathFolder;
    private String encoding = "UTF-8";
    private TemplateExceptionHandler templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER;
    private boolean logTemplateExceptions = false;

    public FreemarkerEngineBuilder templateFolder(File templateFolder) {
        this.templateFolder = templateFolder;
        return this;
    }

    public FreemarkerEngineBuilder classpathFolder(String classpathFolder) {
        this.classpathFolder = classpathFolder;
        return this;
    }

    public FreemarkerEngineBuilder encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public FreemarkerEngineBuilder templateExceptionHandler(TemplateExceptionHandler templateExceptionHandler) {
        this.templateExceptionHandler = templateExceptionHandler;
        return this;
    }

    public FreemarkerEngineBuilder logTemplateExceptions(boolean logTemplateExceptions) {
        this.logTemplateExceptions = logTemplateExceptions;
        return this;
    }

    public FreemarkerEngine build() {
        if ((templateFolder == null) || (classpathFolder == null)) {
            throw new IllegalArgumentException("templateFolder or classpathFolder should be configured");
        }

        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);

            if (templateFolder != null) {
                configuration.setDirectoryForTemplateLoading(templateFolder);
            } else if (classpathFolder != null) {
                configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), classpathFolder));

            }

            configuration.setDefaultEncoding(encoding);
            configuration.setTemplateExceptionHandler(templateExceptionHandler);
            configuration.setLogTemplateExceptions(logTemplateExceptions);
            return new FreemarkerEngine(configuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
