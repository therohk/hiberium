package com.konivax.models.template;

import com.konivax.RenderProject;
import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.models.ModelFactory;
import com.konivax.models.Project;
import com.konivax.models.mapper.DatabaseMapper;
import com.konivax.utils.FileUtils;
import com.konivax.utils.format.CsvUtils;
import com.konivax.utils.format.FtlUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * validate all templates available under resources
 * note that is this does not check compilation in target language
 */
public class FtlTemplateTest {

    @Test
    public void testRenderTemplates() {

        String projectPath = FileUtils.getProjectBase();
        System.out.println("project base: "+projectPath);
        String configPath = projectPath + "/hiberium-gen/src/main/resources/";

        Project project = ModelFactory.createSampleProject();
        List<Concept> conceptList = CsvUtils.readCsvFileData(configPath+"concept-def.csv", Concept.class);
        List<Attribute> attributeList = CsvUtils.readCsvFileData(configPath+"attribute-xref.csv", Attribute.class);
        RenderProject.attachConceptAttributes(conceptList, attributeList);
        RenderProject.attachConceptRelations(conceptList, attributeList);

        Map<String,Object> root = new HashMap<String,Object>();
        root.putAll(project.exportProjectToModel());
        root.putAll(DatabaseMapper.mapDatabaseToDriver("h2"));

        //scan available templates
        System.out.println("scanning templates: "+configPath);
        List<String> templates = FileUtils.findFilesRecursive(configPath);
        List<String> projectTemplates = new ArrayList<String>();
        List<String> conceptTemplates = new ArrayList<String>();

        for(String template : templates) {
            if(!template.endsWith(".ftl"))
                continue;
            template = template.substring(configPath.length());
            if(template.startsWith("projection")) {
                template = template.substring("projection".length() + 1);
                projectTemplates.add(template);
            }
            if(template.startsWith("conception")) {
                template = template.substring("conception".length() + 1);
                conceptTemplates.add(template);
            }
        }

        System.out.println("found projections: "+projectTemplates.size());
        System.out.println("found conceptions: "+conceptTemplates.size());

        //render all project templates
        for(String template : projectTemplates) {
            System.out.println("rendering projection: "+template);
            FtlUtils.parseNamedTemplate(root, template);
        }

        //render all concept templates
        //pick a concept with children
        Concept conceptUse = conceptList.stream()
                .filter(c -> c.getConceptName().equals("WorkflowProcess"))
                .findFirst().get();
        Map<String,Object> conceptData = conceptUse.exportConceptToModel();
        conceptData.putAll(root);
        for(String template : conceptTemplates) {
            System.out.println("rendering conception: "+template);
            FtlUtils.parseNamedTemplate(conceptData, template);
        }
    }
}
