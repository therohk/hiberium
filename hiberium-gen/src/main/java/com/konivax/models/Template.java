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

    //source details
    private String template;
    private String sourcePath; //blank for classpath template
    //target details
    private String packagePath;
    private String filename;
    private String condition;

    private List<String> dependencies;

    public Template() { }

    public static String getTemplateBaseName(String template) {
        if(template.endsWith(".ftl"))
            return template.substring(0, template.length()-4);
        return template;
    }

    /**
     * generate name for injected dependency
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
     */
    public static String getTemplateFileName(String variable) {
        if(!variable.startsWith("render_"))
            return null;
        String templateFile = variable.substring(7)+".ftl";
        templateFile = templateFile.replaceAll("[_]", "\\-");
        return templateFile;
    }

    /**
     * @deprecated set manually in yaml instead of detecting
     */
    @Deprecated
    public List<String> findTemplateDependencies() {
        if(!template.endsWith(".ftl"))
            return null;
        Set<String> variables = FtlUtils.getTemplateVariables(template);
        if(variables == null || variables.isEmpty())
            return null;
        List<String> dependencies = variables.stream()
                .filter(v -> v.startsWith("render_"))
                .map(Template::getTemplateFileName)
                .collect(Collectors.toList());
//        setDependencies(dependencies);
        return dependencies;
    }

}
