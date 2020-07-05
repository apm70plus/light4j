package com.light.generator.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.light.generator.AbstractGenerator;
import com.light.generator.ContentsFilter;
import com.light.generator.utils.Configuration;

public class DtoGenerator extends AbstractGenerator {

    private ContentsFilter filter;

    private final String modelSource;

    public DtoGenerator(final Configuration config) {
        super(config, "dto");
        final String modelSource = this.getFileString(this.config.getModelSrcPath());
        this.modelSource = this.initApiDocs(modelSource);
        this.initFilter();
    }

    // 初始化API文档信息
    private String initApiDocs(final String source) {
        final Pattern pattern = Pattern.compile("(/\\*\\*\\s+\\*\\s)(.+)(\\s+\\*/)");
        String result = source;
        final Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            final String originalStr = matcher.group(0);
            final String docStr = matcher.group(2);
            result = result.replace(originalStr,
                    originalStr + System.lineSeparator() + "    @ApiModelProperty(\"" + docStr + "\")");
        }
        return result;
    }

    @Override
    public void generate() {
        final String value = this.filter.filter(this.modelSource);
        this.output(value);
    }

    private void initFilter() {
    	boolean isLongTypeId = this.config.isLongTypeId();
        final Map<String, String> filterMap = new HashMap<String, String>();
        String packageStr = "package " + this.getPackage("dto") + ";";
        if (isLongTypeId) {
        	packageStr = packageStr + System.lineSeparator() + "import com.light.web.dto.AbstractDTO;";
        } else {
        	packageStr += System.lineSeparator() + "import com.fasterxml.jackson.annotation.JsonIgnore;";
        	packageStr += System.lineSeparator() + "import java.io.Serializable;";
        }
        filterMap.put("package.+;", packageStr);
        filterMap.put("import javax\\.persistence.+\\s+", "");
        filterMap.put("import com\\.light\\.jpa\\.domain.+",
                "import io.swagger.annotations.ApiModel;"+ System.lineSeparator() + "import io.swagger.annotations.ApiModelProperty;");
        filterMap.put("@Entity", "@ApiModel");
        filterMap.put("@Table.+\\s+", "");
        //filterMap.put("\\s.+serialVersionUID.+\\s+", "    private static final long serialVersionUID = 1L;");
        filterMap.put("\\n\\s+@Column.+", "");
        filterMap.put("\\n\\s+@Enumerated.+", "");
        filterMap.put("\\n\\s+@OneToOne.+", "");
        filterMap.put("\\n\\s+@OneToMany.+", "");
        filterMap.put("\\n\\s+@ManyToMany.+", "");
        filterMap.put("\\n\\s+@ManyToOne.+", "");
        filterMap.put("\\n\\s+@Table.+", "");
        filterMap.put("\\n\\s+@Temporal.+", "");
        String dtoClass = "";
        if (isLongTypeId) {
            dtoClass = "public class @Model@DTO extends AbstractDTO {".replace("@Model@",
                    this.config.getModelClazz().getSimpleName());
        } else {
        	dtoClass = "public class @Model@DTO implements Serializable {".replace("@Model@",
                    this.config.getModelClazz().getSimpleName());
        	dtoClass += System.lineSeparator() + "	@ApiModelProperty(value = \"主键ID\", position = 0)";
        	dtoClass += System.lineSeparator() + "    private String id;";
        	dtoClass += System.lineSeparator() + System.lineSeparator() + "    @JsonIgnore";
        	dtoClass += System.lineSeparator() + "    public boolean isNew() {";
        	dtoClass += System.lineSeparator() + "        return this.id == null;";
        	dtoClass += System.lineSeparator() + "    }";
        }

        filterMap.put("public class.+\\{", dtoClass);
        this.filter = new ReplaceFilter(filterMap);
    }

}
