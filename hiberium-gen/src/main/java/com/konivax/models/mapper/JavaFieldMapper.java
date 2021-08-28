package com.konivax.models.mapper;

import com.konivax.models.Attribute;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.konivax.models.mapper.FieldConstants.*;

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
            case "mediumint":
            case "smallint":
            case "tinyint":
            case "serial":
                return TYPE_INTEGER;
            case "long":
            case "int8":
            case "serial8":
                return TYPE_LONG;
            case "date":
            case "time":
            case "datetime":
            case "instant":
            case "timestamp":
            case "timestamptz":
                return TYPE_DATETIME;
            case "number":
            case "decimal":
            case "float":
            case "double":
            case "real":
            case "numeric":
                return TYPE_DOUBLE;
            case "char":
            case "bpchar":
                return TYPE_CHARACTER;
            case "string":
            case "nchar":
            case "varchar":
            case "nvarchar":
            case "text":
                return TYPE_STRING;
            case "bool":
            case "boolean":
            case "bit":
                return TYPE_BOOLEAN;
        }
        return null;
    }

    public static Class<?> mapJavaFieldTypeToClass(String javaFieldType) {
        switch (javaFieldType) {
            case TYPE_STRING:
                return String.class;
            case TYPE_INTEGER:
                return Integer.class;
            case TYPE_LONG:
                return Long.class;
            case TYPE_DOUBLE:
                return Double.class;
            case TYPE_DATETIME:
                return Date.class;
            case TYPE_CHARACTER:
                return Character.class;
            case TYPE_BOOLEAN:
                return Boolean.class;
        }
        return null;
    }

    public static String mapJavaFieldTypeToPackage(String javaFieldType) {
        switch (javaFieldType) {
            case TYPE_STRING:
                return String.class.getName();
            case TYPE_INTEGER:
                return Integer.class.getName();
            case TYPE_LONG:
                return Long.class.getName();
            case TYPE_DOUBLE:
                return Double.class.getName();
            case TYPE_DATETIME:
                return Date.class.getName();
            case TYPE_CHARACTER:
                return Character.class.getName();
            case TYPE_BOOLEAN:
                return Boolean.class.getName();
        }
        return null;
    }

}
