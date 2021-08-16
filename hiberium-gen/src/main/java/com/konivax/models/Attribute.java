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

    //java api options
    @Column(name = "attribute_name")
    private String attributeName;
    @Column(name = "attribute_flag")
    private String attributeConfig;
    @Column(name = "attribute_format")
    private String attributeFormat;
    @Column(name = "attribute_java")
    private String javaFieldType;
    private String javaFullFieldType;

    //database options
    @Column(name = "field_name")
    private String fieldName;
    @Column(name = "field_type")
    private String fieldType;
    private Integer fieldStartPos; //for fixed width source
    @Column(name = "field_scale")
    private Integer fieldScale; //length for varchar type
    @Column(name = "field_precision")
    private Integer fieldPrecision;

    @Column(name = "default_value")
    private String defaultValue;
    private String fieldBehavior;
    private String setterValue; //for default value expression
    private String sequenceName; //db sequence for id generation

    @Column(name = "foreign_key")
    private String foreignKey;
    @Column(name = "foreign_key_table")
    private String foreignKeyTable;
    @Column(name = "foreign_key_field")
    private String foreignKeyField;
    private String foreignConstraintName;
    private String foreignKeyType;

    //elastic options
    @Column(name = "elastic_type")
    private String elasticIndexType; //for elastic index json
    @Column(name = "elastic_doctype")
    private String elasticDocType; //for elastic document java

    private String formFieldType;

    @Column(name = "enable_coldef")
    private Boolean includeColumnDefinition;

    //nullable; searchable; indexable; analysable

    public void applyScalePrecision(Integer scale, Integer precision) {
        this.fieldScale = scale;
        this.fieldPrecision = precision;
    }

    public boolean hasAttributeFlag(String attributeFlag) {
        if(StringUtils.isBlank(attributeConfig))
            return false;
        return attributeConfig.contains(attributeFlag);
    }

    public void applyAttributeFlag(String attributeFlag) {
        if(StringUtils.isBlank(attributeConfig))
            attributeConfig = attributeFlag;
        if(!attributeConfig.contains(attributeFlag))
            attributeConfig = attributeConfig + attributeFlag;
    }

    public void createDerivedNames() {
        //todo validate

        if(StringUtils.isBlank(fieldName)) {
            List<String> parts = StringUtils.splitByCharacterType(attributeName, true);
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
            elasticDocType = ElasticFieldMapper.mapElasticTypeToDocumentType(this);
        }
        if(StringUtils.notBlank(foreignKey)) {
            foreignKeyTable = foreignKey.split("\\.", 2)[0];
            foreignKeyField = foreignKey.split("\\.", 2)[1];
            foreignConstraintName = fieldName+"_fk";
            if(!attributeConfig.contains("M"))
                attributeConfig = attributeConfig + "M";
        }

    }

}
