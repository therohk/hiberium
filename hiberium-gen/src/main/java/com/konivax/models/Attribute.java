package com.konivax.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.konivax.models.mapper.JavaFieldMapper;
import com.konivax.utils.CollectionUtils;
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
    private String fieldType;

    @Column(name = "attribute_type")
    private String attributeType;
    @Column(name = "attribute_flag")
    private String attributeConfig;

    @Column(name = "attribute_java")
    private String javaFieldType;
    private String javaFullFieldType;
    private String elasticFieldType;

    private Integer fieldStartPos;
    private Integer fieldLength;
    private Integer fieldPrecision;

    private String defaultValue;

    private String foreignKeyTable;
    private String foreignKeyField;
    private String foreignConstraintName;

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
        List<String> parts = CollectionUtils.arrayToList(StringUtils.splitByCharacterType(attributeName, true));

        if(StringUtils.isBlank(fieldName)) {
            fieldName = parts.stream()
                    .map(s -> s.toLowerCase())
                    .collect(Collectors.joining("_"));
        }
        if(StringUtils.isBlank(attributeConfig)) {
            attributeConfig = "S";
        }
        if(StringUtils.isBlank(javaFieldType)) {
            javaFieldType = JavaFieldMapper.mapDatabaseToJavaFieldType(attributeType);
            javaFullFieldType = JavaFieldMapper.mapJavaFieldTypeToPackage(javaFieldType);
        }
    }

}
