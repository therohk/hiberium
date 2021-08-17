package com.konivax.files;

import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.StringUtils;
import com.konivax.utils.Validate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CsvValidator {

    public static void validateConcepts(List<Concept> concepts) {

        for(Concept concept : concepts) {
            String conceptName = concept.getConceptName();

            Validate.isTrue(StringUtils.notBlank(concept.getConceptName()), "concept name not defined");
            Validate.isTrue(StringUtils.notBlank(concept.getModuleName()), "module name not defined");

            Validate.isTrue(StringUtils.notBlank(concept.getSqlTableName()), "table name not defined");
            Validate.isTrue(StringUtils.notBlank(concept.getSqlSchemaName()), "schema name not defined");

            Validate.isTrue(CollectionUtils.notEmpty(concept.getAttributeXref()), "attributes not defined");

            long attributeCount = concept.getAttributeXref().size();
            long primaryKeys = concept.getAttributeXref().stream()
                    .filter(a -> a.getAttributeRole().contains("K"))
                    .count();
            Validate.isFalse(primaryKeys <= 0, "primary key not found for "+conceptName);
            Validate.isFalse(primaryKeys >= 2, "multiple primary keys found for "+conceptName);

            Set<String> attributeNameSet = concept.getAttributeXref().stream()
                    .map(Attribute::getAttributeName)
                    .collect(Collectors.toSet());
            Validate.isTrue(attributeCount == attributeNameSet.size(), "duplicate attribute name");

            Set<String> fieldNameSet = concept.getAttributeXref().stream()
                    .map(Attribute::getFieldName)
                    .collect(Collectors.toSet());
            Validate.isTrue(attributeCount == fieldNameSet.size(), "duplicate field name");
        }


    }

}
