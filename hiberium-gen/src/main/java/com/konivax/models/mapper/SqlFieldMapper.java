package com.konivax.models.mapper;

import com.konivax.models.Attribute;

public final class SqlFieldMapper {

    private SqlFieldMapper() { }

    public static String mapDatabaseToFieldType(Attribute attribute) {
        String fieldType = attribute.getFieldType();

        switch (fieldType.toLowerCase()) {
            case "numeric":
                fieldType = "NUMERIC("+attribute.getFieldLength()+","+attribute.getFieldPrecision()+")";
                return fieldType;

        }
        return null;
    }

    public static String createNullString(Attribute attribute) {
        if(attribute.getAttributeConfig().contains("N"))
            return "NOT NULL";
        else
            return "NULL";
    }
}
