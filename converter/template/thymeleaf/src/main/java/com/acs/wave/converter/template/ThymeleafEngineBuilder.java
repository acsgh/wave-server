package com.acs.wave.converter.template;

import com.acs.wave.utils.CheckUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

public class ThymeleafEngineBuilder {

    public enum Source {
        File, Classpath
    }

    private Source source = Source.Classpath;
    private String prefix;
    private String suffix = ".html";
    private TemplateMode templateMode = TemplateMode.HTML;
    private String encoding = "UTF-8";

    public ThymeleafEngineBuilder source(Source source) {
        this.source = source;
        return this;
    }

    public ThymeleafEngineBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ThymeleafEngineBuilder suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public ThymeleafEngineBuilder templateMode(TemplateMode templateMode) {
        this.templateMode = templateMode;
        return this;
    }

    public ThymeleafEngineBuilder encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public ThymeleafEngine build() {
        CheckUtils.checkNull("source", source);
        CheckUtils.checkString("prefix", prefix);
        CheckUtils.checkString("suffix", suffix);
        CheckUtils.checkNull("templateMode", templateMode);
        CheckUtils.checkString("encoding", encoding);

        try {
            TemplateEngine configuration = new TemplateEngine();

            AbstractConfigurableTemplateResolver templateResolver;
            switch (source) {
                case File:
                    templateResolver = new FileTemplateResolver();
                    break;
                case Classpath:
                    templateResolver = new ClassLoaderTemplateResolver();
                    break;
                default:
                    throw new NullPointerException("Source " + source + " is not valid");
            }
            templateResolver.setTemplateMode(templateMode);
            templateResolver.setPrefix(prefix);
            templateResolver.setSuffix(suffix);
            templateResolver.setCharacterEncoding(encoding);

            configuration.setTemplateResolver(templateResolver);

            return new ThymeleafEngine(configuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
