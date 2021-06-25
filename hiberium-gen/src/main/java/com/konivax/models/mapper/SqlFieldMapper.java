package com.konivax.models.mapper;

import com.konivax.models.Attribute;

public class SqlFieldMapper {

    public static String mapDatabaseToFieldType(Attribute attribute) {
        String fieldType = attribute.getFieldType();

        switch (fieldType.toLowerCase()) {
            case "numeric":
                fieldType = "NUMERIC("+attribute.getFieldLength()+","+attribute.getFieldPrecision()+")";
                return fieldType;

        }
        return null;
    }

}
