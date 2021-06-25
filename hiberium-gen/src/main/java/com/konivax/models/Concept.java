package com.konivax.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.konivax.utils.CollectionUtils;
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

    @Column(name = "concept_varname")
    private String variableName;
    @Column(name = "concept_apipath")
    private String contextName;
    @Column(name = "concept_class")
    private String className;

    private String repositoryPath;
    private String entityPath;
    private String controllerPath;
    private String servicePath;
    @Column(name = "update_code")
    private String updateCode;

    private Boolean selectable = true;
    private Boolean insertable = true;
    private Boolean updateable = true;
    private Boolean deleteable = true;

    @Transient
    private List<Attribute> attributeXref;


    public void createDerivedNames() {
        List<String> parts = CollectionUtils.arrayToList(StringUtils.splitByCharacterType(conceptName, true));

        //variable name
        if(StringUtils.isBlank(variableName)) {
            variableName = StringUtils.toFirstLowerCase(conceptName);
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
