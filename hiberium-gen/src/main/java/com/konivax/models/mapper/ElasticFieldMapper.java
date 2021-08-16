package com.konivax.models.mapper;

import com.konivax.models.Attribute;

import static com.konivax.models.mapper.FieldConstants.*;

public final class ElasticFieldMapper {

    private ElasticFieldMapper() { }

    /**
     * generate field type for elastic index config
     */
    public static String mapJavaFieldTypeToElastic(Attribute attribute) {
        String javaFieldType = attribute.getJavaFieldType();
        switch (javaFieldType) {
            case TYPE_STRING:
                Integer fieldLength = attribute.getFieldScale();
                if(fieldLength != null && fieldLength <= 15)
                    return "keyword";
                else
                    return "text";
            case TYPE_INTEGER:
            case TYPE_LONG:
                return "long";
            case "Float":
            case TYPE_DOUBLE:
                return "double";
            case TYPE_DATETIME:
                return "date";
            case TYPE_BOOLEAN:
                return "boolean";
        }
        return null;
    }

    /**
     * generate field type for java elastic document
     */
    public static String mapElasticTypeToDocumentType(Attribute attribute) {
        String typePrefix = "FieldType.";
        String elasticType = attribute.getElasticIndexType();
        switch (elasticType) {
            case "keyword":
                return typePrefix+"Keyword";
            case "text":
                return typePrefix+"Text";
            case "long":
                return typePrefix+"Long";
            case "double":
                return typePrefix+"Double";
            case "date":
                return typePrefix+"Date";
            case "boolean":
                return typePrefix+"Boolean";
        }
       return typePrefix+"Auto";
    }

//    Auto,
//    Text,
//    Keyword,
//    Long,
//    Integer,
//    Short,
//    Byte,
//    Double,
//    Float,
//    Half_Float,
//    Scaled_Float,
//    Date,
//    Date_Nanos,
//    Boolean,
//    Binary,
//    Integer_Range,
//    Float_Range,
//    Long_Range,
//    Double_Range,
//    Date_Range,
//    Ip_Range,
//    Object,
//    Nested,
//    Ip,
//    TokenCount,
//    Percolator,
//    Flattened,
//    Search_As_You_Type,
//    Rank_Feature,
//    Rank_Features

}