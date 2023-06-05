package com.konivax.models.mapper;

import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.models.Project;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.StringUtils;
import com.konivax.utils.Validate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ModelValidator {

    public static final String REGEXP_CLASS = "^[A-Z][0-9a-zA-Z]+$";
    public static final String REGEXP_FIELD = "^[a-z][0-9a-zA-Z]+$";
    public static final String REGEXP_MODULE = "^[a-z]+$";
    public static final String REGEXP_PACKAGE = "^[a-z\\.]+$";
    public static final String REGEXP_TABLE = "^[0-9a-z_]+$";
    public static final String REGEXP_HYPEN = "^[0-9a-z\\-]+$";
    public static final String REGEXP_ALNUM = "^[0-9a-z]+$";
    public static final String REGEXP_SYMBOL = "^[A-Z]+$";
    public static final String REGEXP_NUMBER = "^[0-9]+$";
    public static final String REGEXP_VERSION = "^[0-9A-Z\\.\\-]+$";
    public static final String REGEXP_URLPATH = "^/[0-9a-zA-Z\\.\\-/]+$";

    public static final String ALLOW_ROLES = "^[KHUFNIRS]+$";
    public static final String ALLOW_CODES = "[NHDIUBYC]";

    public static final String[] ALLOW_SERVER_DB = {"h2", "sybase", "postgres", "mysql", "oracle", "db2", "sqlserver"};
    public static final String[] ALLOW_SERVER_APP = {"tomcat", "jetty", "undertow"};

    private ModelValidator() { }

    public static void validateModel(List<Concept> concepts) {

        validateAllConcepts(concepts);

        for(Concept concept : concepts) {
            validateSingleConcept(concept);

            for(Attribute attribute : concept.getAttributeXref())
                validateSingleAttribute(attribute);

            validateConceptAttributes(concept);
        }
    }

    public static void validateProject(Project project) {
        Validate.isMatch(project.getProjectName(), REGEXP_HYPEN);
        Validate.isMatch(project.getPackageBase(), REGEXP_PACKAGE);
        Validate.isMatch(project.getArtifactVersion(), REGEXP_VERSION);

        Validate.isOption(project.getDatabaseType(), ALLOW_SERVER_DB);
        Validate.isMatch(project.getProjectSchema(), REGEXP_TABLE);

        Validate.isOption(project.getServerType(), ALLOW_SERVER_APP);
        Validate.isMatch(project.getServerPort(), REGEXP_NUMBER);
        Validate.isMatch(project.getContextBase(), REGEXP_URLPATH);
    }

    private static void validateAllConcepts(List<Concept> concepts) {
        long conceptCount = concepts.size();

        Map<String,String> conceptTableMap = concepts.stream()
                .collect(Collectors.toMap(c -> c.getConceptName().toLowerCase(), Concept::getSqlTableName));
        Validate.isTrue(conceptCount == conceptTableMap.size(),
                "duplicate concept name");
        Validate.isTrue(conceptCount == Set.copyOf(conceptTableMap.values()).size(),
                "duplicate sql table name");

        Set<String> invalidRelationSet = concepts.stream()
                .filter(c -> StringUtils.notBlank(c.getConceptParent()))
                .map(c -> c.getConceptParent().toLowerCase())
                .filter(s -> !conceptTableMap.containsKey(s))
                .collect(Collectors.toSet());
        Validate.isTrue(invalidRelationSet.isEmpty(),
                "missing parent concept name");

        Map<String,Attribute> attributeFieldMap = concepts.stream()
                .flatMap(c -> c.getAttributeXref().stream())
                .collect(Collectors.toMap(a -> conceptTableMap.get(a.getConceptName().toLowerCase()) +"."+a.getFieldName(), a -> a));

        List<Attribute> invalidForeignKey = concepts.stream()
                .flatMap(c -> c.getAttributeXref().stream())
                .filter(a -> StringUtils.notBlank(a.getForeignKey()))
                .filter(a -> !attributeFieldMap.containsKey(a.getForeignKey()))
                .collect(Collectors.toList());
        Validate.isTrue(invalidForeignKey.isEmpty(),
                "missing foreign key field");
    }

    public static void validateSingleConcept(Concept concept) {
        String conceptName = concept.getConceptName();
        //check required fields
        Validate.isMatch(conceptName, REGEXP_CLASS);
        Validate.isMatch(concept.getModuleName(), REGEXP_MODULE);
        Validate.isMatch(concept.getContextName(), REGEXP_HYPEN);

        Validate.isMatch(concept.getSqlTableName(), REGEXP_TABLE);
        Validate.isMatch(concept.getSqlSchemaName(), REGEXP_TABLE);
        Validate.isMatch(concept.getConceptIndex(), REGEXP_TABLE);
        Validate.isMatch(concept.getConceptSymbol(), REGEXP_SYMBOL);

        //reserved keywords
        Validate.isTrue(JavaFieldMapper.validateJavaFieldName(conceptName),
                "illegal concept name for "+conceptName);
        Validate.isTrue(DatabaseMapper.validateSqlFieldName(conceptName),
                "illegal concept name for "+conceptName);

        if(StringUtils.notBlank(concept.getUpdateCode()))
            Validate.isMatch(concept.getUpdateCode(), ALLOW_CODES);
    }

    public static void validateSingleAttribute(Attribute attribute) {
        String attributeName = attribute.getAttributeName();
        String conceptName = attribute.getConceptName();
        Validate.isMatch(attributeName, REGEXP_FIELD);
        Validate.isMatch(conceptName, REGEXP_CLASS);
        Validate.isTrue(JavaFieldMapper.validateJavaFieldName(attributeName),
                "illegal attribute name for "+attributeName);
        Validate.isTrue(DatabaseMapper.validateSqlFieldName(attributeName),
                "illegal attribute name for "+attributeName);

        Validate.isMatch(attribute.getFieldType(), REGEXP_ALNUM);
        Validate.isMatch(attribute.getFieldName(), REGEXP_TABLE);
        if(StringUtils.notBlank(attribute.getForeignKey())) {
            Validate.isMatch(attribute.getForeignKeyTable(), REGEXP_TABLE);
            Validate.isMatch(attribute.getForeignKeyField(), REGEXP_TABLE);
        }

        if(StringUtils.notBlank(attribute.getAttributeRole()))
            Validate.isTrue(attribute.getAttributeRole().length() <= 8,
                    "invalid attribute role for "+attributeName);
        if(StringUtils.notBlank(attribute.getUpdateCode()))
            Validate.isTrue(attribute.getUpdateCode().length() == 1,
                    "strategy code should be single char for "+attributeName);

        Validate.notNull(JavaFieldMapper.mapJavaFieldTypeToClass(attribute.getJavaFieldType()),
                "unknown type for attribute "+attributeName);

        if(StringUtils.notBlank(attribute.getAttributeRole()))
            Validate.isMatch(attribute.getAttributeRole(), ALLOW_ROLES);

        if(StringUtils.notBlank(attribute.getUpdateCode()))
            Validate.isMatch(attribute.getUpdateCode(), ALLOW_CODES);

        if(attribute.getFieldStartPos() != null)
            Validate.isTrue(attribute.getFieldStartPos() > 0 && attribute.getFieldStartPos() <= 8192,
                    "field start pos out of bounds for "+attributeName);

        if(attribute.getFieldScale() != null && FieldConstants.TYPE_STRING.equals(attribute.getJavaFieldType()))
            Validate.isTrue(attribute.getFieldScale() > 0 && attribute.getFieldScale() <= 1024,
                    "length out of bounds for "+attributeName+". consider using text");

        if(attribute.getFieldScale() != null && FieldConstants.TYPE_DOUBLE.equals(attribute.getJavaFieldType()))
            Validate.isTrue(attribute.getFieldScale() > 0 && attribute.getFieldScale() <= 56,
                    "scale out of bounds for "+attributeName);
        if(attribute.getFieldPrecision() != null)
            Validate.isTrue(attribute.getFieldPrecision() > 0 && attribute.getFieldPrecision() <= 128,
                    "precision out of bounds for "+attributeName);
    }

    public static void validateConceptAttributes(Concept concept) {
        String conceptName = concept.getConceptName();
        List<Attribute> attributeList = concept.getAttributeXref();
        Validate.isTrue(CollectionUtils.notEmpty(attributeList), "attributes not defined");

        //check duplicate names
        long attributeCount = attributeList.size();
        Set<String> attributeNameSet = concept.getAttributeXref().stream()
                .map(a -> a.getAttributeName().toLowerCase())
                .collect(Collectors.toSet());
        Validate.isTrue(attributeCount > 0 && attributeCount <= 1024,
                "attribute count out of bounds for "+conceptName);
        Validate.isTrue(attributeCount == attributeNameSet.size(),
                "duplicate attribute name in "+conceptName);

        Set<String> fieldNameSet = attributeList.stream()
                .map(Attribute::getFieldName)
                .collect(Collectors.toSet());
        Validate.isTrue(attributeCount == fieldNameSet.size(),
                "duplicate sql field_name in "+conceptName);

        //check primary key
        long primaryKeys = attributeList.stream()
                .filter(a -> a.getAttributeRole().contains(FieldConstants.ROLE_PRIMARY))
                .count();
        Validate.isFalse(primaryKeys <= 0, "primary key not found for "+conceptName);
        Validate.isFalse(primaryKeys >= 2, "multiple primary keys found for "+conceptName);

        Attribute attributePrimary = attributeList.stream()
                .filter(a -> a.getAttributeRole().contains(FieldConstants.ROLE_PRIMARY))
                .findFirst().get();
        Validate.isTrue(FieldConstants.TYPE_PRIMARY.equals(attributePrimary.getJavaFieldType()),
                "invalid primary key type for "+conceptName);

    }

}
