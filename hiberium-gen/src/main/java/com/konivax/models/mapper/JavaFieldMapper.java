package com.konivax.models.mapper;

import com.konivax.models.Attribute;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * database field type to java field type mapping
 */
public final class JavaFieldMapper {

    private static List<String> javaKeywords = Arrays.asList(
            "abstract", "continue", "for", "new", "switch",
            "assert", "default", "goto", "package", "synchronized",
            "boolean", "do", "if", "private", "this",
            "break", "double", "implements", "protected", "throw",
            "byte", "else", "import", "public", "throws",
            "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void",
            "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while" );

    private JavaFieldMapper() { }

    public static Boolean validateJavaFieldName(String fieldName) {
        return !javaKeywords.contains(fieldName);
    }

    public static String mapStringToBoolean(String strValue, boolean invert) {
        if(StringUtils.isBlank(strValue))
            return null;
        strValue = strValue.toLowerCase();
        if(CollectionUtils.in(strValue, "true", "yes", "Y", "T", "J", "1"))
            return invert ? "false" : "true";
        if(CollectionUtils.in(strValue, "false", "no", "N", "F", "0"))
            return invert ? "true" : "false";
        return null;
    }

    public static String mapDatabaseToJavaFieldType(Attribute attribute) {
        return mapDatabaseToJavaFieldType(attribute.getFieldType());
    }

    public static String mapDatabaseToJavaFieldType(String fieldType) {
        switch (fieldType.toLowerCase()) {
            case "int":
            case "int4":
            case "integer":
            case "smallint":
            case "tinyint":
            case "serial":
                return "Integer";
            case "long":
            case "int8":
            case "serial8":
                return "Long";
            case "date":
            case "time":
            case "datetime":
            case "instant":
            case "timestamp":
            case "timestamptz":
                return "Date";
            case "number":
            case "decimal":
            case "float":
            case "double":
            case "real":
            case "numeric":
                return "Double";
            case "string":
            case "char":
            case "nchar":
            case "varchar":
            case "nvarchar":
            case "text":
                return "String";
            case "bool":
            case "boolean":
            case "bit":
                return "Boolean";
        }
        return null;
    }

    public static Class<?> mapJavaFieldTypeToClass(String javaFieldType) {
        switch (javaFieldType) {
            case "String":
                return String.class;
            case "Integer":
                return Integer.class;
            case "Long":
                return Long.class;
            case "Double":
                return Double.class;
            case "Date":
                return Date.class;
            case "Boolean":
                return Boolean.class;
        }
        return null;
    }

    public static String mapJavaFieldTypeToPackage(String javaFieldType) {
        switch (javaFieldType) {
            case "String":
                return String.class.getName();
            case "Integer":
                return Integer.class.getName();
            case "Long":
                return Long.class.getName();
            case "Double":
                return Double.class.getName();
            case "Date":
                return Date.class.getName();
            case "Boolean":
                return Boolean.class.getName();
        }
        return null;
    }

}
