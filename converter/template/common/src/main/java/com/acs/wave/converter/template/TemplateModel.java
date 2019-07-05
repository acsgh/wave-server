package com.acs.wave.converter.template;

import java.util.HashMap;

public class TemplateModel extends HashMap<String, Object> {

    public final String templateName;

    public TemplateModel(String templateName) {
        this.templateName = templateName;
    }
}
