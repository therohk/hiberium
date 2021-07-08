package com.konivax;

import com.konivax.files.CsvLoader;
import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.models.Project;
import com.konivax.models.Template;
import com.konivax.utils.*;
import com.konivax.utils.format.FtlUtils;
import com.konivax.utils.format.JsonUtils;
import com.konivax.utils.format.YamlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderProject {

    public static void main(String[] args) {

        String projectPath = FileUtils.getCurrentPath("hiberium");
        String configPath = projectPath+"\\hiberium-gen\\src\\main\\resources\\";
        String projectSource = configPath+"hibernate-render.yaml";
        String conceptSource = configPath+"concept-def.csv";
        String attributeSource = configPath+"attribute-xref.csv";

        //load project config
        Project project = YamlUtils.deserializeFile(projectSource, Project.class);
        System.out.println(JsonUtils.serializeJavaObject(project));

        //load data model
        List<Concept> conceptList = CsvLoader.loadConceptDefFile(conceptSource);
        List<Attribute> attributeList = CsvLoader.loadAttributeXrefFile(attributeSource);
        CsvLoader.attachConceptAttributes(conceptList, attributeList);

        //create freemarker model
        String targetPath = projectPath+"\\"+project.getProjectName()+"\\";
        Map<String,Object> root = new HashMap<String,Object>();
        root.putAll(ReflectUtils.toColumnObjectMap(project));

        //process project files
        System.out.println("processing common project files");
        for(Template template : project.getProjections()) {
            flushNamedTemplate(root, targetPath, template);
        }

        //process concept files
        for(Concept concept : conceptList) {
            System.out.println("processing concept " + concept.getConceptName() + " with " + concept.getAttributeXref().size() + " attributes");

            Map<String, Object> conceptData = exportConceptToModel(concept);
            conceptData.putAll(root);

            for (Template template : project.getConceptions()) {
                flushNamedTemplate(conceptData, targetPath, template);
            }
        }

    }

    private static Map<String,Object> exportConceptToModel(Concept concept) {
        Validate.notNull(concept, "concept is not defined");
        Map<String,Object> model = new HashMap<String,Object>();

        concept.createDerivedNames();
        model.putAll(ReflectUtils.toColumnObjectMap(concept));

        List<Map<String,Object>> attributes = new ArrayList<Map<String,Object>>();
        for(Attribute attribute : concept.getAttributeXref()) {
            attribute.createDerivedNames();
            attributes.add(ReflectUtils.toColumnObjectMap(attribute));
        }
        model.put("attributes", attributes);
        return model;
    }


    public static String flushNamedTemplate(final Map<String,Object> dataModel, String basePath, Template template) {

        if(CollectionUtils.isEmpty(template.getDependencies()))
            return flushNamedTemplate(dataModel, template.getTemplate(), basePath, template.getPackagePath(), template.getFilename());

        String packagePath = FtlUtils.parseLocalTemplate(dataModel, template.getPackagePath());
        String fileName = FtlUtils.parseLocalTemplate(dataModel, template.getFilename());

        Map<String,Object> localData = new HashMap<String,Object>();
        for(String dependency : template.getDependencies()) {
            String templateName = FtlUtils.getTemplateBaseName(dependency);
            String templateData = FtlUtils.parseNamedTemplate(dataModel, dependency);

            String embedName = "render_"+templateName.replaceAll("[\\-]", "_");
            localData.put(embedName, templateData);
        }
        localData.putAll(dataModel);

        String folderPath = FileUtils.getFilePath(basePath, packagePath);
        String filePath = FileUtils.getFilePath(basePath, packagePath, fileName);

        FileUtils.createFolder(folderPath, true);
        FtlUtils.flushNamedTemplate(localData, template.getTemplate(), filePath);

        Validate.isTrue(FileUtils.exists(filePath), "template render failed");
        return filePath;
    }

    public static String flushNamedTemplate(final Map<String,Object> dataModel, String templateName,
                                            String basePath, String packagePath, String fileName) {

        String packagePathRender = FtlUtils.parseLocalTemplate(dataModel, packagePath);
        String fileNameRender = FtlUtils.parseLocalTemplate(dataModel, fileName);

        String folderPath = FileUtils.getFilePath(basePath, packagePathRender);
        String filePath = FileUtils.getFilePath(basePath, packagePathRender, fileNameRender);

        FileUtils.createFolder(folderPath, true);
        FtlUtils.flushNamedTemplate(dataModel, templateName, filePath);

        Validate.isTrue(FileUtils.exists(filePath), "template render failed");
        return filePath;
    }

}
