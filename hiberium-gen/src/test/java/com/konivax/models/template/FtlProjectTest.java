package com.konivax.models.template;

import com.konivax.RenderProject;
import com.konivax.models.*;
import com.konivax.models.mapper.DatabaseMapper;
import com.konivax.utils.FileUtils;
import com.konivax.utils.format.FtlUtils;
import com.konivax.utils.format.JsonUtils;
import com.konivax.utils.format.YamlUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * validate all templates included in project
 * uses test data for concept and attribute definitions
 */
public class FtlProjectTest {

    @Test
    public void testDefinedTemplates() {

        String projectPath = FileUtils.getProjectBase();
        System.out.println("project base: "+projectPath);
        String configPath = projectPath+"/hiberium-gen/src/main/resources/";

        Project project = YamlUtils.deserializeFile(configPath+"hibernate-render.yaml", Project.class);
        System.out.println(JsonUtils.serializeJavaObject(project));

        List<Concept> conceptList = new ArrayList<Concept>();
        List<Attribute> attributeList = new ArrayList<Attribute>();

        conceptList.add(ModelFactory.createSampleConcept());
        RenderProject.attachConceptRelations(conceptList, attributeList);
        Concept conceptUse = conceptList.get(0);

        Map<String,Object> root = new HashMap<String,Object>();
        root.putAll(project.exportProjectToModel());
        root.putAll(DatabaseMapper.mapDatabaseToDriver("h2"));

        Set<String> templateSet = new HashSet<String>();

        //validate projections
        for (Template template : project.getProjections()) {
            String templateName = template.getTemplate();
            Assertions.assertTrue(templateSet.add(templateName),
                    "duplicate projection source: "+templateName);
            Assertions.assertTrue(templateSet.add(template.getPackagePath()+"."+template.getFilename()),
                    "duplicate projection target: "+templateName);

            if(FtlUtils.parseLocalExpression(root, template.getCondition(), true))
                FtlUtils.parseNamedTemplate(root, templateName);
        }

        //validate conceptions
        Map<String,Object> conceptData = conceptUse.exportConceptToModel();
        conceptData.putAll(root);
        for (Template template : project.getConceptions()) {
            String templateName = template.getTemplate();
            Assertions.assertTrue(templateSet.add(templateName),
                    "duplicate conception source: "+templateName);
            Assertions.assertTrue(templateSet.add(template.getPackagePath()+"."+template.getFilename()),
                    "duplicate conception target: "+templateName);

            if(FtlUtils.parseLocalExpression(conceptData, template.getCondition(), true))
                FtlUtils.parseNamedTemplate(conceptData, templateName);
        }

    }
}
