package com.konivax.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.konivax.models.mapper.ModelLocator;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author therohk 2021/06/21
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Concept {

    @Id
    @Column(name = "concept_id")
    private Integer conceptId;
    @Column(name = "concept_name")
    private String conceptName;
    @Column(name = "module_name")
    private String moduleName;
    @Column(name = "concept_desc")
    private String conceptDesc;

    @Column(name = "concept_table")
    private String sqlTableName;
    @Column(name = "concept_schema")
    private String sqlSchemaName;
    @Column(name = "concept_index")
    private String conceptIndex;

    @Column(name = "concept_varname")
    private String variableName;
    @Column(name = "concept_apipath")
    private String contextName;
    @Column(name = "update_code")
    private String updateCode;
    @Column(name = "concept_perpage")
    private Integer conceptPage;
    @Column(name = "concept_access")
    private String conceptAccess; //not implemented

    @Column(name = "dynamic_insert")
    private Boolean dynamicInsert = true;
    @Column(name = "dynamic_update")
    private Boolean dynamicUpdate = false;

    //for composites, requires foreign key
    //unique key decides cardinality
    @Column(name = "concept_parent")
    private String conceptParent;
    //for relational algebra
    @Column(name = "concept_symbol")
    private String conceptSymbol;

    @Transient
    private List<Attribute> attributeXref;
    @Transient
    private List<Attribute> relationXref;

    public Concept() {
        attributeXref = new ArrayList<Attribute>();
        relationXref = new ArrayList<Attribute>();
    }

    public void createDerivedNames() {
        List<String> parts = StringUtils.splitByCharacterType(conceptName, true);

        //variable name
        if(StringUtils.isBlank(variableName)) {
            variableName = StringUtils.uncapFirst(conceptName);
        }
        //table name
        if(StringUtils.isBlank(sqlTableName)) {
            sqlTableName = parts.stream()
                    .map(s -> s.toLowerCase())
                    .collect(Collectors.joining("_"));
        }
        //index name
        if(StringUtils.isBlank(conceptIndex)) {
            conceptIndex = sqlTableName;
        }
        //context path
        if(StringUtils.isBlank(contextName)) {
            contextName = parts.stream()
                    .map(s -> s.toLowerCase())
                    .collect(Collectors.joining("-"));
        }
        //concept symbol
        if(StringUtils.isBlank(conceptSymbol)) {
            conceptSymbol = parts.stream()
                    .map(s -> s.substring(0, 1))
                    .map(s -> s.toUpperCase())
                    .collect(Collectors.joining(""));
        }
    }

    public void addAttribute(Attribute attribute) {
        if(CollectionUtils.isEmpty(attributeXref))
            attributeXref = new ArrayList<Attribute>();
        attribute.setConceptName(conceptName);
        attribute.setAttributeOrder(attributeXref.size());
        attributeXref.add(attribute);
    }

    public void generateHiberiumLocation() {
        //tbd integration with visual schema
        String conceptRef = moduleName+"."+conceptName;
        double[] location = ModelLocator.locateObject(conceptRef);
        Double longitude = location[0];
        Double latitude = location[1];
    }
}
