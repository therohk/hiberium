package com.konivax.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

/**
 * @author therohk 2021/06/25
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    private String projectType;
    @Column(name = "project_name")
    private String projectName;
    private String projectTitle;
    private String projectDesc;
    private String projectPath;

    @Column(name = "package_base")
    private String packageBase;
    @Column(name = "artifact_version")
    private String artifactVersion;
    private String databaseType;
    @Column(name = "project_schema")
    private String projectSchema;

    //tomcat jetty undertow
    @Column(name = "server_type")
    private String serverType;
    @Column(name = "server_port")
    private String serverPort;
    @Column(name = "context_base")
    private String contextBase;

    //rendered once per project
    private List<Template> projections;
    //rendered once per concept
    private List<Template> conceptions;

}
