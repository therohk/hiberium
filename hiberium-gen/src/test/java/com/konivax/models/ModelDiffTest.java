package com.konivax.models;

import com.konivax.RenderProject;
import com.konivax.files.FtlBuilder;
import com.konivax.models.mapper.DatabaseMapper;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.FileUtils;
import com.konivax.utils.HashUtils;
import com.konivax.utils.StringUtils;
import com.konivax.utils.format.CsvUtils;
import com.konivax.utils.format.FtlUtils;
import com.konivax.utils.format.JsonUtils;
import com.konivax.utils.format.YamlUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * run manually to detect changes in generated output
 * compares the md5 hash of output files to current project model
 */
public class ModelDiffTest {

    @Test
    @Disabled
    public void showProjectDiff() {

        String projectPath = FileUtils.getProjectBase();
        String sourcePath = projectPath+"/hiberium-gen/";
        String configPath = projectPath+"/hiberium-gen/src/main/resources/";
        System.out.println("project base: "+projectPath);

        Project project = YamlUtils.deserializeFile(configPath+"hibernate-render.yaml", Project.class);
        System.out.println(JsonUtils.serializeJavaObject(project));
        String targetPath = projectPath + "/" + project.getProjectName() + "/";
        if(!FileUtils.exists(targetPath)) {
            System.out.println("target project not found");
            return;
        }

        //build project model
        List<Concept> conceptList = CsvUtils.readCsvFileData(configPath+"concept-def.csv", Concept.class);
        List<Attribute> attributeList = CsvUtils.readCsvFileData(configPath+"attribute-xref.csv", Attribute.class);
        RenderProject.attachConceptAttributes(conceptList, attributeList);
        RenderProject.attachConceptRelations(conceptList, attributeList);

        Map<String, Object> root = new HashMap<String, Object>();
        root.putAll(project.exportProjectToModel());
        root.putAll(DatabaseMapper.mapDatabaseToDriver(project.getDatabaseType()));

        //process project files
        Map<String, String> newHash = new LinkedHashMap<String, String>();
        System.out.println("processing common project files");
        for (Template template : project.getProjections()) {
            String filePath = FtlBuilder.renderFilePath(root, "", template.getPackagePath(), template.getFilename());
            String fileData = renderFtlTemplateLocal(root, template);
            if(!StringUtils.isEmpty(fileData))
                newHash.put(filePath, HashUtils.getCheckSum(fileData));
        }
        for (Concept concept : conceptList) {
            System.out.println("processing concept " + concept.getConceptName() + " with " + concept.getAttributeXref().size() + " attributes");
            Map<String, Object> conceptData = concept.exportConceptToModel();
            conceptData.putAll(root);
            for (Template template : project.getConceptions()) {
                String filePath = FtlBuilder.renderFilePath(conceptData, "", template.getPackagePath(), template.getFilename());
                String fileData = renderFtlTemplateLocal(conceptData, template);
                if(!StringUtils.isEmpty(fileData))
                    newHash.put(filePath, HashUtils.getCheckSum(fileData));
            }
        }

        //scan existing project
        Map<String,String> oldHash = new LinkedHashMap<String,String>();
        System.out.println("scanning folder: "+targetPath);
        List<String> generated = FileUtils.findFilesRecursive(targetPath);
        for(String fileName : generated) {
            String fileHash = HashUtils.fileCheckSum(fileName);
            fileName = getRelativePath(targetPath, fileName);
            if(fileName.startsWith("build/"))
                continue;
            oldHash.put(fileName, fileHash);
        }

        //show project diff
        System.out.println("found old: "+oldHash.size()+" in "+targetPath);
        System.out.println("built new: "+newHash.size()+" in memory");

        System.out.println("## project removals");
        for(Map.Entry<String,String> entry : oldHash.entrySet()) {
            if(!newHash.containsKey(entry.getKey()))
                System.out.println("- "+entry.getKey());
        }
        System.out.println("## project additions");
        for(Map.Entry<String,String> entry : newHash.entrySet()) {
            if(!oldHash.containsKey(entry.getKey()))
                System.out.println("+ "+entry.getKey());
        }
        System.out.println("## project modifications");
        for(Map.Entry<String,String> entry : oldHash.entrySet()) {
            if(!newHash.containsKey(entry.getKey()))
                continue;
            String oldVal = entry.getValue();
            String newVal = newHash.get(entry.getKey());
            if(!oldVal.equals(newVal))
                System.out.println("~ "+entry.getKey()+" ( "+oldVal+" != "+newVal+" )");
        }
    }

    private static String getRelativePath(String basePath, String filePath) {
        basePath = basePath.replaceAll("/", "\\\\");
        if(filePath.startsWith(basePath))
            filePath = filePath.substring(basePath.length());
        filePath = filePath.replaceAll("\\\\", "/");
        return filePath;
    }

    public static String renderFtlTemplateLocal(final Map<String,Object> dataModel, Template template) {
        if(!FtlUtils.parseLocalExpression(dataModel, template.getCondition(), true))
            return null;

        //todo support non ftl files
//        if(!templateName.endsWith(".ftl"))
//            return renderFileSimple(dataModel,
//                    sourceBase, template.getSourcePath(), template.getTemplate(),
//                    basePath, template.getPackagePath(), template.getFilename());

        if(CollectionUtils.isEmpty(template.getDependencies()))
            return FtlUtils.parseNamedTemplate(dataModel, template.getTemplate());

        Map<String,Object> localData = new HashMap<String,Object>();
        for(String dependency : template.getDependencies()) {
            String templateData = FtlUtils.parseNamedTemplate(dataModel, dependency);
            String embedName = Template.getTemplateEmbedName(dependency);
            localData.put(embedName, templateData);
        }
        localData.putAll(dataModel);

        return FtlUtils.parseNamedTemplate(localData, template.getTemplate());
    }
}
