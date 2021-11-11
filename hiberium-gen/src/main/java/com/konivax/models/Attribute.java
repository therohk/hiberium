package com.konivax.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.konivax.models.mapper.ElasticFieldMapper;
import com.konivax.models.mapper.FieldConstants;
import com.konivax.models.mapper.JavaFieldMapper;
import com.konivax.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author therohk 2021/06/21
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attribute {

    @Id
    @Column(name = "attribute_id")
    private Integer attributeId;
    @Column(name = "attribute_order")
    private Integer attributeOrder;
    @Column(name = "concept_name")
    private String conceptName;

    @Column(name = "attribute_name")
    private String attributeName;
    @Column(name = "attribute_desc")
    private String attributeDesc;
    @Column(name = "attribute_role")
    private String attributeRole;
    @Column(name = "attribute_format")
    private String attributeFormat;
    @Column(name = "attribute_java")
    private String javaFieldType;
    private String javaFullFieldType;
    @Column(name = "update_code")
    private String updateCode;

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
    private String foreignKeyType; // = FieldConstants.TYPE_PRIMARY;

    @Column(name = "elastic_type")
    private String elasticIndexType; //for elastic index json

    private String formFieldType;
    private String fieldQuantity;
    private String fieldUnit;

    public Attribute() { }

    public void applyScalePrecision(Integer precision, Integer scale) {
        this.fieldPrecision = precision;
        this.fieldScale = scale;
    }

    public boolean hasAttributeFlag(String attributeFlag) {
        if(StringUtils.isBlank(attributeRole))
            return false;
        return attributeRole.contains(attributeFlag);
    }

    public void applyAttributeFlag(String attributeFlag) {
        if(StringUtils.isBlank(attributeRole))
            attributeRole = attributeFlag;
        if(!attributeRole.contains(attributeFlag))
            attributeRole = attributeRole + attributeFlag;
    }

    public void createDerivedNames() {
        if(StringUtils.isBlank(fieldName)) {
            List<String> parts = StringUtils.splitByCharacterType(attributeName, true);
            fieldName = parts.stream()
                    .map(s -> s.toLowerCase())
                    .collect(Collectors.joining("_"));
        }
        if(StringUtils.isBlank(attributeRole)) {
            attributeRole = FieldConstants.ROLE_DEFAULT;
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
            foreignConstraintName = fieldName+"_"+FieldConstants.KEY_FOREIGN;
            applyAttributeFlag(FieldConstants.ROLE_FOREIGN);
        }

    }

}
