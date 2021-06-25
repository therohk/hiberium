package com.konivax.render;

import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.utils.*;
import com.konivax.utils.format.FtlUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderTemplateTest {

    @Test
    public void testRenderConceptTemplates() {


        List<String> split = CollectionUtils.arrayToList(StringUtils.splitByCharacterType("ConceptOne", true));
        System.out.println(split.toString());

        Map<String,Object> model = new HashMap<String,Object>();
        model.put("concept_name", "HereThere");
        model.put("concept_varname", "hereThere");
        model.put("concept_apipath", "here-there");

        model.put("package_base", "com.konivax");
        model.put("module_name", "sample");

        //crud - create read update delete
        //simd - select insert merge delete
        //pdgp - post delete get put

        System.out.println("## GET /all");
        System.out.println(FtlUtils.parseNamedTemplate(model, "controller-selectall-java.ftl"));

        System.out.println("## GET /id");
        System.out.println(FtlUtils.parseNamedTemplate(model, "controller-select-java.ftl"));

        System.out.println("## POST json");
        System.out.println(FtlUtils.parseNamedTemplate(model, "controller-insert-java.ftl"));

        System.out.println("## PUT /id json");
        System.out.println(FtlUtils.parseNamedTemplate(model, "controller-update-java.ftl"));

        System.out.println("## DELETE /id");
        System.out.println(FtlUtils.parseNamedTemplate(model, "controller-delete-java.ftl"));

//        System.out.println("## ENDPOINT");
//        System.out.println(FtlUtils.parseNamedTemplate(model, "controller-shell-java.ftl"));

    }

    @Test
    public void testConceptGenerate() {


        Concept conceptData = new Concept();
        conceptData.setConceptName("HereThere");
        conceptData.setVariableName("hereThere");
        conceptData.setControllerPath("here-there");
        conceptData.setModuleName("module");

        String basePackage = "com.konivax";

        Map<String,Object> model = new HashMap<String,Object>();
        model.putAll(ReflectUtils.toColumnObjectMap(conceptData));

        model.put("package_base", basePackage);

        String basePath = "C:\\Users\\Roh\\intelspace\\hiberium\\hiberium-war\\src\\main\\java\\";

        flushNamedTemplate(model, "spring-repository-java.ftl", basePath,
                FileUtils.getPackage(basePackage,"jpa.repository", conceptData.getModuleName()),
                conceptData.getConceptName()+"Repository.java");

        flushNamedTemplate(model, "service-interface-java.ftl", basePath,
                FileUtils.getPackage(basePackage,"service", conceptData.getModuleName()),
                conceptData.getConceptName()+"Service.java");
    }

    @Test
    public void testConceptLocation() throws IOException {

        Concept conceptData = new Concept();
        conceptData.setConceptName("HereThere");
        conceptData.setVariableName("hereThere");
        conceptData.setControllerPath("here-there");
        conceptData.setModuleName("module");


        Map<String,Object> model = new HashMap<String,Object>();
        model.putAll(ReflectUtils.toColumnObjectMap(conceptData));


        String basePackage = "com.konivax";
//        String packagePath = basePackage+".jpa.repository"+".module";
        String packagePath = FileUtils.getPackage(basePackage,"jpa.repository", conceptData.getModuleName());
        String repoName = conceptData.getConceptName()+"Repository";
        String repoFileName = repoName+".java";

        String basePath = "C:\\Users\\Roh\\intelspace\\hiberium\\hiberium-war\\src\\main\\java\\";

        System.out.println(basePath);
        System.out.println(packagePath);
        System.out.println(repoFileName);

//        System.out.println(System.getProperty("user.dir"));
//        System.out.println(new File(".").getAbsolutePath());
//        System.out.println(new File(".").getCanonicalPath());

        FileUtils.createFolder(FileUtils.getFilePath(basePath, packagePath), true);

        String fileName = FileUtils.getFilePath(basePath, packagePath, repoFileName);

        System.out.println(fileName);

        model.put("package_base", basePackage);

        FtlUtils.flushNamedTemplate(model, "spring-repository-java.ftl", fileName);

    }

    public static void flushNamedTemplate(final Map<String,Object> dataModel, String templateName,
                                          String basePath, String packagePath, String fileName) {

        String folderPath = FileUtils.getFilePath(basePath, packagePath);
        String classPath = FileUtils.getFilePath(basePath, packagePath, fileName);

        FileUtils.createFolder(folderPath, true);

        FtlUtils.flushNamedTemplate(dataModel, templateName, classPath);

    }


    @Test
    public void testRenderAttributeTemplates() {

        Concept conceptData = new Concept();
        conceptData.setConceptName("HereThere");
        conceptData.setVariableName("hereThere");
        conceptData.setControllerPath("here-there");

        Attribute attribute = new Attribute();
        attribute.setAttributeId(1);
        attribute.setAttributeName("");


        List<Attribute> attributeList = new ArrayList<Attribute>();

        Map<String,Object> model = new HashMap<String,Object>();
        model.putAll(ReflectUtils.toColumnObjectMap(conceptData));

//        model.put("attributes", attributeList);

//        System.out.println(FtlUtils.parseNamedTemplate(model, "jpa-entity-java.ftl"));

//        System.out.println(FtlUtils.parseNamedTemplate(model, "create-table-sql.ftl"));
    }




}
