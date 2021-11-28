package com.konivax.models;

import com.konivax.RenderProject;
import com.konivax.files.FtlBuilder;
import com.konivax.utils.FileUtils;
import com.konivax.utils.ReflectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * manually render a single file using this sample test
 * generated at given path under hiberium-test
 */
public class ManualRenderTest {

    @Test
    @Disabled
    public void testRenderSingleTemplate() {

        Project project = ModelFactory.createSampleProject();
        Concept concept = ModelFactory.createSampleConcept();

        Map<String,Object> dataModel = new HashMap<String,Object>();
        dataModel.putAll(ReflectUtils.toColumnObjectMap(project));
        dataModel.putAll(RenderProject.exportConceptToModel(concept));

        String projectPath = FileUtils.getProjectBase();
        String sourcePath = projectPath + "\\hiberium-gen\\";
        String targetPath = projectPath + "\\" + project.getProjectName() + "\\";

        //ftl render template
        Template template = new Template();
        template.setTemplate("create-table-sql.ftl");
        template.setPackagePath("src.main.resources.database");
        template.setFilename("test-${concept_table}.sql");

        String templatePath = FtlBuilder.renderFtlTemplate(dataModel, sourcePath, targetPath, template);
        Assertions.assertTrue(FileUtils.exists(templatePath));

        //file copy template
        Template fileCopy = new Template();
        fileCopy.setTemplate("attribute-xref.csv");
        fileCopy.setSourcePath("src.main.resources");
        fileCopy.setPackagePath("src.main.resources.schema");
        fileCopy.setFilename("${project_name}-attribute-xref.csv");

        String fileCopyPath = FtlBuilder.renderFtlTemplate(dataModel, sourcePath, targetPath, fileCopy);
        Assertions.assertTrue(FileUtils.exists(fileCopyPath));
    }
}
