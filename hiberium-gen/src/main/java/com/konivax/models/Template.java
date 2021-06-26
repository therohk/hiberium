package com.konivax.models;

import com.konivax.utils.format.FtlUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author therohk 2021/06/25
 */
@Getter
@Setter
public class Template {

    private String template;
    private String packagePath;
    private String filename;

    private List<String> dependencies;

    private String sourcePath;
    private String targetPath;


    public void findTemplateDependencies() {
        Set<String> variables = FtlUtils.getTemplateVariables(template);
        if(variables == null || variables.isEmpty())
            return;
        dependencies = variables.stream()
                .filter(v -> v.startsWith("render_"))
                .map(v -> v.substring(8)+".ftl")
                .map(v -> v.replaceAll("[_]", "\\-"))
                .collect(Collectors.toList());
        dependencies.addAll(variables);
    }

    public String getTemplateEmbedName(String templateName) {
        if(templateName.endsWith(".ftl"))
            templateName = templateName.substring(0, templateName.length()-4);
        String embedName = "render_"+templateName.replaceAll("[\\-]", "_");
        return embedName;
    }
}
