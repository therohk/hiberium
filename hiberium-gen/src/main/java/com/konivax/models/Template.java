package com.konivax.models;

import com.konivax.utils.format.FtlUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Template {

    private String template;
    private String packagePath;
    private String filename;

    private List<String> dependencies;

    private String sourcePath = "/freemarker/";
    private String targetPath;


    public void findTemplateDependencies() {
        Set<String> variables = FtlUtils.getTemplateVariables(template);
        if(variables == null || variables.isEmpty())
            return;
        dependencies = variables.stream()
                .filter(v -> v.startsWith("render-"))
                .map(v -> v.substring(8)+".ftl")
                .map(v -> v.replaceAll("[\\-]", "_"))
                .collect(Collectors.toList());
        dependencies.addAll(variables);
    }

}
