package com.konivax.files;

import com.konivax.models.Concept;
import com.konivax.utils.StringUtils;
import com.konivax.utils.Validate;

import java.util.List;

public class CsvValidator {

    public static void validateConcepts(List<Concept> concepts) {

        for(Concept concept : concepts) {
            String conceptName = concept.getConceptName();

            Validate.isTrue(StringUtils.notBlank(concept.getConceptName()), "concept name not defined");
            Validate.isTrue(StringUtils.notBlank(concept.getModuleName()), "module name not defined");

            Validate.isTrue(StringUtils.notBlank(concept.getSqlTableName()), "table name not defined");
            Validate.isTrue(StringUtils.notBlank(concept.getSqlSchemaName()), "schema name not defined");

            long primaryKeys = concept.getAttributeXref().stream()
                    .filter(a -> a.getAttributeConfig().contains("K"))
                    .count();
            Validate.isFalse(primaryKeys <= 0, "primary key not found for "+conceptName);
            Validate.isFalse(primaryKeys >= 2, "multiple primary keys found for "+conceptName);


        }


    }

}
