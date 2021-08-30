package com.konivax;

import com.konivax.files.FtlBuilder;
import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.models.Project;
import com.konivax.models.Template;
import com.konivax.models.mapper.ModelValidator;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.FileUtils;
import com.konivax.utils.ReflectUtils;
import com.konivax.utils.format.CsvUtils;
import com.konivax.utils.format.JsonUtils;
import com.konivax.utils.format.YamlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RenderProject {

    public static void main(String[] args) {
        String projectPath = FileUtils.getProjectBase();
        String configPath = projectPath + "\\hiberium-gen\\src\\main\\resources\\";
        String projectYaml = configPath + "hibernate-render.yaml";
        String conceptCsv = configPath + "concept-def.csv";
        String attributeCsv = configPath + "attribute-xref.csv";

        RenderProject.renderFromConfig(projectPath, projectYaml, conceptCsv, attributeCsv);
    }

    public static void renderFromConfig(String projectPath, String projectYaml, String conceptCsv, String attributeCsv) {
        //load project config
        Project project = YamlUtils.deserializeFile(projectYaml, Project.class);
        System.out.println(JsonUtils.serializeJavaObject(project));

        //load data model
        List<Concept> conceptList = CsvUtils.readCsvFileData(conceptCsv, Concept.class);
        List<Attribute> attributeList = CsvUtils.readCsvFileData(attributeCsv, Attribute.class);
        attachConceptAttributes(conceptList, attributeList);
        ModelValidator.validateModel(conceptList);

        //create freemarker model
        Map<String, Object> root = new HashMap<String, Object>();
        root.putAll(ReflectUtils.toColumnObjectMap(project));

        //code source and target
        String sourcePath = projectPath + "\\hiberium-gen\\";
        String targetPath = projectPath + "\\" + project.getProjectName() + "\\";

        //process project files
        System.out.println("processing common project files");
        for (Template template : project.getProjections()) {
            FtlBuilder.renderFtlTemplate(root, sourcePath, targetPath, template);
        }

        //process concept files
        for (Concept concept : conceptList) {
            System.out.println("processing concept " + concept.getConceptName() + " with " + concept.getAttributeXref().size() + " attributes");

            Map<String, Object> conceptData = exportConceptToModel(concept);
            conceptData.putAll(root);

            for (Template template : project.getConceptions()) {
                FtlBuilder.renderFtlTemplate(conceptData, sourcePath, targetPath, template);
            }
        }
    }

    public static void attachConceptAttributes(List<Concept> conceptList, List<Attribute> attributeList) {
        for (Concept concept : conceptList) {
            List<Attribute> attributeReq = attributeList.stream()
                    .filter(a -> concept.getConceptName().equals(a.getConceptName()))
                    .collect(Collectors.toList());
            AtomicInteger attrIndex = new AtomicInteger(0);
            attributeReq.forEach(a -> a.setAttributePos(attrIndex.getAndIncrement()));
            concept.setAttributeXref(attributeReq);
        }
        //verify and derive
        for(Concept concept : conceptList) {
            if(CollectionUtils.isEmpty(concept.getAttributeXref()))
                throw new IllegalStateException("no attributes found for concept "+concept.getConceptName());
            concept.createDerivedNames();
        }
        for(Attribute attribute : attributeList) {
            if(attribute.getAttributePos() == null)
                throw new IllegalStateException("no concept found for attribute " +
                        attribute.getConceptName()+"."+attribute.getAttributeName());
            attribute.createDerivedNames();
        }
    }

    public static Map<String, Object> exportConceptToModel(Concept concept) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.putAll(ReflectUtils.toColumnObjectMap(concept));

        List<Map<String, Object>> attributes = new ArrayList<Map<String, Object>>();
        for (Attribute attribute : concept.getAttributeXref())
            attributes.add(ReflectUtils.toColumnObjectMap(attribute));
        model.put("attributes", attributes);

        return model;
    }

}
