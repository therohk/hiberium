package com.konivax.models;

import com.konivax.RenderProject;
import com.konivax.models.mapper.ModelValidator;
import com.konivax.utils.FileUtils;
import com.konivax.utils.format.CsvUtils;
import com.konivax.utils.format.YamlUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * check if provided model fits constraints
 */
public class ModelValidateTest {

    @Test
    public void testValidateModel() {
        String projectPath = FileUtils.getProjectBase();
        System.out.println("project base: "+projectPath);
        String configPath = projectPath+"/hiberium-gen/src/main/resources/";

        Project project = YamlUtils.deserializeFile(configPath+"hibernate-render.yaml", Project.class);
        List<Concept> conceptList = CsvUtils.readCsvFileData(configPath+"concept-def.csv", Concept.class);
        List<Attribute> attributeList = CsvUtils.readCsvFileData(configPath+"attribute-xref.csv", Attribute.class);

        RenderProject.attachConceptAttributes(conceptList, attributeList);
        RenderProject.attachConceptRelations(conceptList, attributeList);

        ModelValidator.validateProject(project);
        ModelValidator.validateModel(conceptList);
    }
}
