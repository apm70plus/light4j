package com.light.generator.impl;

import java.util.HashMap;
import java.util.Map;

import com.light.generator.AbstractGenerator;
import com.light.generator.ContentsFilter;
import com.light.generator.utils.Configuration;

public class ConvertorGenerator extends AbstractGenerator {
    // 模板路径
    private static final String templatePath = "/codetemplate/Convertor.template";
    private static final String templatePath2 = "/codetemplate/Convertor2.template";

    private ContentsFilter filter;

    private final String templateContents;
    private String setModelCode;
    private String setDTOCode;

    public ConvertorGenerator(final Configuration config) {
        super(config, "convertor");
        this.initConvertingCode();
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
        final Map<String, String> filterMap = new HashMap<String, String>();
        filterMap.put("@Package@", this.getPackage("convertor"));
        filterMap.put("@DTOPath@", this.getClassPath("dto"));
        filterMap.put("@ModelPath@", this.getModelPath());
        filterMap.put("@ServicePath@", this.getClassPath("service"));
        filterMap.put("@Model@", this.getModelName());
        filterMap.put("@model@", this.getModelNameWithHeadLow());
        filterMap.put(" +@setModelCode@", this.setModelCode);
        filterMap.put(" +@setDTOCode@", this.setDTOCode);
        this.filter = new ReplaceFilter(filterMap);
    }

    private void initConvertingCode() {
        final StringBuilder setModelCode = new StringBuilder();
        final StringBuilder setDTOCode = new StringBuilder();
        this.config.getModelProperties().getProperties().forEach(property -> {
            setModelCode.append("        model.").append(property.getSetter()).append("(dto.")
            .append(property.getGetter())
            .append("());").append(System.lineSeparator());
            setDTOCode.append("        dto.").append(property.getSetter()).append("(model.")
            .append(property.getGetter())
            .append("());").append(System.lineSeparator());
        });
        this.setModelCode = setModelCode.toString();
        this.setDTOCode = setDTOCode.toString();
    }
}
