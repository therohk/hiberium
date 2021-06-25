package com.konivax.models.mapper;

import java.util.Date;

/**
 * database field type to java field type mapping
 */
public class JavaFieldMapper {

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
                return "Long";
            case "date":
            case "time":
            case "datetime":
            case "instant":
            case "timestamp":
            case "timestampz":
                return "Date";
            case "number":
            case "decimal":
            case "float":
            case "double":
            case "real":
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

    public static Class<?> mapJavaFieldTypeToClass(String fieldType) {
        switch (fieldType) {
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

    public static String mapJavaFieldTypeToPackage(String fieldType) {
        switch (fieldType) {
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
