package com.konivax.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.konivax.models.mapper.ElasticFieldMapper;
import com.konivax.models.mapper.JavaFieldMapper;
import com.konivax.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author therohk 2021/06/21
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attribute {

    @Column(name = "attribute_id")
    private Integer attributeId;
    @Column(name = "concept_name")
    private String conceptName;

    @Column(name = "attribute_name")
    private String attributeName;
    @Column(name = "field_name")
    private String fieldName;
    @Column(name = "field_type")
    private String fieldType;

    @Column(name = "attribute_flag")
    private String attributeConfig;
    @Column(name = "attribute_format")
    private String attributeFormat;

    @Column(name = "attribute_java")
    private String javaFieldType;
    private String javaFullFieldType;

    @Column(name = "elastic_index")
    private String elasticIndexType; //for elastic index json
    @Column(name = "elastic_document")
    private String elasticDocType; //for elastic document java

    private Integer fieldStartPos; //for fixed width source
    @Column(name = "field_length")
    private Integer fieldLength; //scale for numeric types
    @Column(name = "field_precision")
    private Integer fieldPrecision;

    @Column(name = "default_value")
    private String defaultValue;
    private String fieldBehavior;
    private String setterValue; //for default value expression
    private String sequenceName; //db sequence for id generation

    @Column(name = "foreign_key")
    private String foreignKey;
    private String foreignKeyTable;
    private String foreignKeyField;
    private String foreignConstraintName;
    private String foreignKeyType;

    private String formFieldType;

    @Column(name = "enable_coldef")
    private Boolean includeColumnDefinition;

    //nullable
    //searchable
    //indexable
    //analysable

    public void applyAttributeFlag(String attributeConfig) {

//        if(attributeConfig.contains("K"))

    }

    public void createDerivedNames() {
        List<String> parts = StringUtils.splitByCharacterType(attributeName, true);

        if(StringUtils.isBlank(fieldName)) {
            fieldName = parts.stream()
                    .map(s -> s.toLowerCase())
                    .collect(Collectors.joining("_"));
        }
        if(StringUtils.isBlank(attributeConfig)) {
            attributeConfig = "S";
        }
        if(StringUtils.isBlank(javaFieldType)) {
            javaFieldType = JavaFieldMapper.mapDatabaseToJavaFieldType(fieldType);
            javaFullFieldType = JavaFieldMapper.mapJavaFieldTypeToPackage(javaFieldType);
        }
        if(StringUtils.isBlank(elasticIndexType)) {
            elasticIndexType = ElasticFieldMapper.mapJavaFieldTypeToElastic(this);
        }
        if(StringUtils.notBlank(foreignKey)) {
            foreignKeyTable = foreignKey.split("\\.", 2)[0];
            foreignKeyField = foreignKey.split("\\.", 2)[1];
            foreignConstraintName = "";
        }

    }

}
