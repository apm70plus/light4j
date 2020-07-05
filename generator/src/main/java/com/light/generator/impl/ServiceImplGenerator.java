package com.light.generator.impl;

import java.util.HashMap;
import java.util.Map;

import com.light.generator.AbstractGenerator;
import com.light.generator.ContentsFilter;
import com.light.generator.utils.Configuration;

public class ServiceImplGenerator extends AbstractGenerator {

    // 模板路径
    private static final String templatePath = "/codetemplate/ServiceImpl.template";
    private static final String templatePath2 = "/codetemplate/ServiceImpl2.template";

    private ContentsFilter filter;

    private final String templateContents;

    public ServiceImplGenerator(final Configuration config) {
        super(config, "serviceImpl");
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
        final String servicePackage = this.getPackage("service") + ".impl";

        final Map<String, String> filterMap = new HashMap<String, String>();
        filterMap.put("@Package@", servicePackage);
        filterMap.put("@ServicePath@", this.getClassPath("service"));
        filterMap.put("@ModelPath@", this.getModelPath());
        filterMap.put("@Model@", this.getModelName());
        filterMap.put("@model@", this.getModelNameWithHeadLow());
        filterMap.put("@RepositoryPath@", this.getClassPath("repository"));
        this.filter = new ReplaceFilter(filterMap);
    }
}
