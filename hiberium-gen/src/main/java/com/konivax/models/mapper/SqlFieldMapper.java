package com.konivax.models.mapper;

import com.konivax.models.Attribute;

public final class SqlFieldMapper {

    private SqlFieldMapper() { }

    public static String mapDatabaseToFieldType(Attribute attribute) {
        String fieldType = attribute.getFieldType();
        String javaType = attribute.getJavaFieldType();
        String derivedType = null;

        switch (fieldType.toLowerCase()) {
            case "numeric":
                derivedType = fieldType.toUpperCase()+"("+attribute.getFieldScale()+","+attribute.getFieldPrecision()+")";
                attribute.setIncludeColumnDefinition(true);
                return derivedType;
            case "varchar":
                derivedType = fieldType.toUpperCase()+"("+attribute.getFieldScale()+")";
                attribute.setIncludeColumnDefinition(true);
                return derivedType;
        }
        return null;
    }

}
