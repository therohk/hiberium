package com.konivax.models.template;

import com.konivax.RenderProject;
import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.models.ModelFactory;
import com.konivax.models.Project;
import com.konivax.models.mapper.DatabaseMapper;
import com.konivax.utils.FileUtils;
import com.konivax.utils.ReflectUtils;
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

        String projectPath = getTestProjectBase();
        System.out.println("project base: "+projectPath);
        String configPath = projectPath + "\\hiberium-gen\\src\\main\\resources\\";

        Project project = ModelFactory.createSampleProject();
        List<Concept> conceptList = CsvUtils.readCsvFileData(configPath+"concept-def.csv", Concept.class);
        List<Attribute> attributeList = CsvUtils.readCsvFileData(configPath+"attribute-xref.csv", Attribute.class);
        RenderProject.attachConceptAttributes(conceptList, attributeList);
        RenderProject.attachConceptRelations(conceptList, attributeList);

        Map<String,Object> root = new HashMap<String,Object>();
        root.putAll(ReflectUtils.toColumnObjectMap(project));
        root.putAll(DatabaseMapper.mapDatabaseToDriver("h2"));

        //scan available templates
        List<String> templates = FileUtils.findFilesRecursive(configPath);
        List<String> projectTemplates = new ArrayList<String>();
        List<String> conceptTemplates = new ArrayList<String>();

        for(String template : templates) {
            if(!template.endsWith(".ftl"))
                continue;
            if(template.startsWith(configPath))
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
        Map<String,Object> conceptData = RenderProject.exportConceptToModel(conceptUse);
        conceptData.putAll(root);
        for(String template : conceptTemplates) {
            System.out.println("rendering conception: "+template);
            FtlUtils.parseNamedTemplate(conceptData, template);
        }
    }

    private static String getTestProjectBase() {
        String workingDir = System.getProperty("user.dir");
        System.out.println("working directory: "+workingDir);
        if(workingDir.endsWith("\\"))
            workingDir = workingDir.substring(0, workingDir.length()-1);
        String subProject = "hiberium-gen";
        if(workingDir.endsWith("\\"+subProject))
            workingDir = workingDir.substring(0, workingDir.length()-subProject.length()-1);
        if(!FileUtils.isFile(workingDir+"\\settings.gradle"))
            throw new RuntimeException("wrong project base folder: "+workingDir);
        return workingDir;
    }
}
