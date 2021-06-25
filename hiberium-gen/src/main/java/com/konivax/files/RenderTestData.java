package com.konivax.files;

import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.models.Project;
import com.konivax.utils.format.JsonUtils;
import com.konivax.utils.ReflectUtils;
import com.konivax.utils.format.YamlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderTestData {


    public static void main(String[] args) {



        String workingDir = System.getProperty("user.dir");
        String projectName = "hiberium";
        String projectPath = workingDir.substring(0, workingDir.indexOf(projectName)+projectName.length());

        System.out.println(workingDir + " " + projectPath);





        String configPath = projectPath+"\\hiberium-gen\\src\\main\\resources\\";

        String basePath = projectPath+"\\hiberium-war\\";
        String basePackage = "com.konivax";

        Project project = YamlUtils.deserializeFile(configPath+"hibernate-render.yaml", Project.class);
        System.out.println(JsonUtils.serializeJavaObject(project));


        List<Concept> conceptList = CsvLoader.loadConceptDefFile(configPath+"concept-def.csv");
        List<Attribute> attributeList = CsvLoader.loadAttributeXrefFile(configPath+"attribute-xref.csv");
        CsvLoader.attachConceptAttributes(conceptList, attributeList);

        //properties

        for(Concept concept : conceptList) {
            concept.createDerivedNames();
            System.out.println("loaded concept "+concept.getConceptName()+" with "+concept.getAttributeXref().size()+" attributes");

            Map<String,Object> model = new HashMap<String,Object>();
            model.put("package_base", basePackage);
            model.putAll(ReflectUtils.toColumnObjectMap(concept));

            List<Map<String,Object>> attributes = new ArrayList<Map<String,Object>>();
            for(Attribute attribute : concept.getAttributeXref()) {
                attribute.createDerivedNames();
                attributes.add(ReflectUtils.toColumnObjectMap(attribute));
            }
            model.put("attributes", attributes);



//            System.out.println(concept.exportToTemplateModel());
            System.out.println(ReflectUtils.toColumnObjectMap(concept));
            System.out.println(model.toString());

            handleConceptRender(model, basePath);

        }



    }

    private static void handleConceptRender(Map<String,Object> model, String basePath) {

        RenderHibernateProject project = new RenderHibernateProject();

        project.renderJpaRepository(model, basePath);

        project.renderJpaEntity(model, basePath);

        project.renderServiceInterface(model, basePath);
//        project.renderServiceImplementation(model, basePath);

        project.renderController(model, basePath);

    }

}
