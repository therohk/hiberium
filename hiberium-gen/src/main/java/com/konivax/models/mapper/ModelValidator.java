package com.konivax.models.mapper;

import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.StringUtils;
import com.konivax.utils.Validate;

import java.util.List;
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

    public static final String ALLOW_ROLES = "^[KHUFNIRS]+$";
    public static final String ALLOW_CODES = "[NHDIUBYC]";

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

    private static void validateAllConcepts(List<Concept> concepts) {
        long conceptCount = concepts.size();

        Set<String> conceptNameSet = concepts.stream()
                .map(Concept::getConceptName)
                .collect(Collectors.toSet());
        Validate.isTrue(conceptCount == conceptNameSet.size(), "duplicate concept name");

        Set<String> tableNameSet = concepts.stream()
                .map(Concept::getSqlTableName)
                .collect(Collectors.toSet());
        Validate.isTrue(conceptCount == tableNameSet.size(),
                "duplicate sql table name");

        Set<String> invalidRelationSet = concepts.stream()
                .filter(c -> c.getConceptParent() != null)
                .map(c -> c.getConceptParent())
                .filter(s -> !conceptNameSet.contains(s))
                .collect(Collectors.toSet());
        Validate.isTrue(invalidRelationSet.isEmpty(),
                "missing parent concept name");
    }

    public static void validateSingleConcept(Concept concept) {
        //check required fields
        Validate.isMatch(concept.getConceptName(), REGEXP_CLASS);
        Validate.isMatch(concept.getModuleName(), REGEXP_MODULE);
        Validate.isMatch(concept.getContextName(), REGEXP_HYPEN);

        Validate.isMatch(concept.getSqlTableName(), REGEXP_TABLE);
        Validate.isMatch(concept.getSqlSchemaName(), REGEXP_TABLE);
        Validate.isMatch(concept.getConceptIndex(), REGEXP_TABLE);
        Validate.isMatch(concept.getConceptSymbol(), REGEXP_SYMBOL);

        if(StringUtils.notBlank(concept.getUpdateCode()))
            Validate.isMatch(concept.getUpdateCode(), ALLOW_CODES);
    }

    public static void validateSingleAttribute(Attribute attribute) {
        String attributeName = attribute.getAttributeName();
        String conceptName = attribute.getConceptName();
        Validate.isMatch(attributeName, REGEXP_FIELD);
        Validate.isMatch(conceptName, REGEXP_CLASS);

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
        if(attribute.getFieldScale() != null)
            Validate.isTrue(attribute.getFieldScale() > 0 && attribute.getFieldScale() <= 1024,
                    "scale out of bounds for "+attributeName+". consider using text");
        if(attribute.getFieldPrecision() != null)
            Validate.isTrue(attribute.getFieldPrecision() > 0 && attribute.getFieldPrecision() <= 15,
                    "precision out of bounds for "+attributeName);
    }

    public static void validateConceptAttributes(Concept concept) {
        String conceptName = concept.getConceptName();
        List<Attribute> attributeList = concept.getAttributeXref();
        Validate.isTrue(CollectionUtils.notEmpty(attributeList), "attributes not defined");

        //check duplicate names
        long attributeCount = attributeList.size();
        Set<String> attributeNameSet = concept.getAttributeXref().stream()
                .map(Attribute::getAttributeName)
                .collect(Collectors.toSet());
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
