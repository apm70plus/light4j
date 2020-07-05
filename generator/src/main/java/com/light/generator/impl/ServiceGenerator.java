package com.light.generator.impl;

import java.util.HashMap;
import java.util.Map;

import com.light.generator.AbstractGenerator;
import com.light.generator.ContentsFilter;
import com.light.generator.utils.Configuration;

public class ServiceGenerator extends AbstractGenerator {

    // 模板路径
    private static final String templatePath = "/codetemplate/Service.template";
    private static final String templatePath2 = "/codetemplate/Service2.template";

    private ContentsFilter filter;

    private final String templateContents;

    public ServiceGenerator(final Configuration config) {
        super(config, "service");
        this.initFilter();
        String template = config.isLongTypeId() ? templatePath : templatePath2;
        this.templateContents = this.getFileString(template);
    }

    @Override
    public void generate() {
        final String value = this.filter.filter(this.templateContents);
        this.output(value);
    }

    private void initFilter() {

        final Map<String, String> serviceFilterMap = new HashMap<String, String>();
        serviceFilterMap.put("@Package@", this.getPackage("service"));
        serviceFilterMap.put("@ModelPath@", this.getModelPath());
        serviceFilterMap.put("@Model@", this.getModelName());
        this.filter = new ReplaceFilter(serviceFilterMap);
    }
}
