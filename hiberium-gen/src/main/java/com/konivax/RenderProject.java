package com.konivax;

import com.konivax.files.FtlBuilder;
import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.models.Project;
import com.konivax.models.Template;
import com.konivax.utils.FileUtils;
import com.konivax.utils.ReflectUtils;
import com.konivax.utils.Validate;
import com.konivax.utils.format.CsvUtils;
import com.konivax.utils.format.JsonUtils;
import com.konivax.utils.format.YamlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RenderProject {

    public static void main(String[] args) {
        String projectPath = FileUtils.getCurrentPath("hiberium");
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

        //create freemarker model
        String targetPath = projectPath + "\\" + project.getProjectName() + "\\";
        Map<String, Object> root = new HashMap<String, Object>();
        root.putAll(ReflectUtils.toColumnObjectMap(project));

        //process project files
        System.out.println("processing common project files");
        for (Template template : project.getProjections()) {
            FtlBuilder.renderFtlTemplate(root, targetPath, template);
        }

        //process concept files
        for (Concept concept : conceptList) {
            System.out.println("processing concept " + concept.getConceptName() + " with " + concept.getAttributeXref().size() + " attributes");

            Map<String, Object> conceptData = exportConceptToModel(concept);
            conceptData.putAll(root);

            for (Template template : project.getConceptions()) {
                FtlBuilder.renderFtlTemplate(conceptData, targetPath, template);
            }
        }
    }

    public static void attachConceptAttributes(List<Concept> conceptList, List<Attribute> attributeList) {
        for (Concept concept : conceptList) {
            String conceptName = concept.getConceptName();
            List<Attribute> attributeReq = attributeList.stream()
                    .filter(a -> a.getConceptName().equals(conceptName))
                    .collect(Collectors.toList());

            concept.setAttributeXref(attributeReq);
        }
    }

    public static Map<String, Object> exportConceptToModel(Concept concept) {
        Validate.notNull(concept, "concept is not defined");
        Map<String, Object> model = new HashMap<String, Object>();

        concept.createDerivedNames();
        model.putAll(ReflectUtils.toColumnObjectMap(concept));

        List<Map<String, Object>> attributes = new ArrayList<Map<String, Object>>();
        for (Attribute attribute : concept.getAttributeXref()) {
            attribute.createDerivedNames();
            attributes.add(ReflectUtils.toColumnObjectMap(attribute));
        }
        model.put("attributes", attributes);
        return model;
    }

}
