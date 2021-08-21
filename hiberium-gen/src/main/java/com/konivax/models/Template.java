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
    private Boolean skipRender = false;

    public static String getTemplateBaseName(String template) {
        if(template.endsWith(".ftl"))
            return template.substring(0, template.length()-4);
        return template;
    }

    /**
     * appears as file render-name1-name2.ftl
     * maps to freemarker variable render_name1_name2
     */
    public static String getTemplateEmbedName(String template) {
        if(template.endsWith(".ftl"))
            template = template.substring(0, template.length()-4);
        String embedName = "render_"+template.replaceAll("[\\-]", "_");
        return embedName;
    }

    /**
     * appears as template variable render_name1_name2
     * maps to resource file render-name1-name2.ftl
     * avoid usage : set manually in yaml instead of detecting
     */
    @Deprecated
    public void findTemplateDependencies() {
        if(!template.endsWith(".ftl"))
            return;
        Set<String> variables = FtlUtils.getTemplateVariables(template);
        if(variables == null || variables.isEmpty())
            return;
        List<String> dependencies = variables.stream()
                .filter(v -> v.startsWith("render_"))
                .map(v -> v.substring(8)+".ftl")
                .map(v -> v.replaceAll("[_]", "\\-"))
                .collect(Collectors.toList());
//        setDependencies(dependencies); not used
    }

}
