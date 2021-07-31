package com.konivax.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.konivax.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
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

    @Column(name = "concept_id")
    private Integer conceptId;
    @Column(name = "concept_name")
    private String conceptName;
    @Column(name = "module_name")
    private String moduleName;

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
    @Column(name = "concept_class")
    private String className;
    @Column(name = "update_code")
    private String updateCode;

    @Column(name = "dynamic_insert")
    private Boolean dynamicInsert = true;
    @Column(name = "dynamic_update")
    private Boolean dynamicUpdate = false;

    //requires foreign key
    @Column(name = "concept_parent")
    private String conceptParent;
    @Column(name = "concept_cardinality")
    private String cardinalityParent; //parent:child is 1:1 or 1:n

    private Boolean selectable = true;
    private Boolean insertable = true;
    private Boolean updateable = true;
    private Boolean deleteable = true;

    @Transient
    private List<Attribute> attributeXref;


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
        //context path
        if(StringUtils.isBlank(contextName)) {
            contextName = parts.stream()
                    .map(s -> s.toLowerCase())
                    .collect(Collectors.joining("-"));
        }
        if(StringUtils.isBlank(updateCode)) {
            updateCode = "B";
        }
    }

    public List<String> listRequiredFunctions() {
        List<String> functions = new ArrayList<String>();
        if(selectable)
            functions.add("select");
        if(insertable)
            functions.add("insert");
        if(updateable)
            functions.add("update");
        if(deleteable)
            functions.add("delete");
        return functions;
    }

    public void generateHiberiumLocation() {

    }
}
