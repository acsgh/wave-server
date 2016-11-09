package com.acs.wave.converter.template;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;


public class FreemarkerEngine {

    public final Configuration configuration;

    FreemarkerEngine(Configuration configuration) {
        this.configuration = configuration;
    }

    public byte[] write(TemplateModel body) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter printStream = new PrintWriter(out)) {

            Template template = configuration.getTemplate(body.templateName, "UTF-8");
            template.process(body, printStream);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
