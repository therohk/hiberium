package com.konivax.models;

import java.util.ArrayList;
import java.util.List;

public final class ModelFactory {

    private ModelFactory() { }

    public static Project createSampleProject() {
        Project project = new Project();
        project.setProjectName("hiberium-test");
        project.setPackageBase("com.konivax");
        project.setContextBase("/hiberium/1.0");
        project.setArtifactVersion("0.0.1-SNAPSHOT");
        project.setProjectSchema("test_schema");
        return project;
    }

    public static Concept createSampleConcept() {
        Concept concept = new Concept();
        concept.setConceptName("TestClass");
        concept.setSqlTableName("test_class");
        concept.setSqlSchemaName("test_schema");
        concept.setModuleName("module");
        concept.setContextName("test-context");

        Attribute attribute1 = new Attribute();
        attribute1.setAttributeName("testAttrKey");
        attribute1.setAttributeRole("K");
        attribute1.setFieldType("serial");
        concept.addAttribute(attribute1);

        Attribute attribute2 = new Attribute();
        attribute2.setAttributeName("testAttrVal");
        attribute2.setAttributeRole("S");
        attribute2.setFieldType("varchar");
        concept.addAttribute(attribute2);

        concept.createDerivedNames();
        concept.getAttributeXref().forEach(Attribute::createDerivedNames);
        return concept;
    }

    public static Concept createMinimalConcept(String conceptName, String module, String schema) {
        Concept concept = new Concept();
        concept.setConceptName(conceptName);
        concept.setSqlSchemaName(schema);
        concept.setModuleName(module);
        concept.createDerivedNames();
        return concept;
    }

    public static List<Concept> createMinimalConceptList(List<String> conceptNameList, String module, String schema) {
        List<Concept> conceptList = new ArrayList<Concept>();
        for(String conceptName : conceptNameList) {
            Concept concept = createMinimalConcept(conceptName, module, schema);
            conceptList.add(concept);
        }
        return conceptList;
    }

    public static Attribute createMinimalAttribute(String attributeName, String fieldType, String fieldRole) {
        Attribute attribute = new Attribute();
        attribute.setAttributeName(attributeName);
        attribute.setFieldType(fieldType);
        attribute.setAttributeRole(fieldRole);
        attribute.createDerivedNames();
        return attribute;
    }
}
