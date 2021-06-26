package com.konivax.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
public class Project {

    private String projectType;
    private String projectName;
    @Column(name = "package_base")
    private String packageBase;
    @Column(name = "context_base")
    private String contextBase;

    @Column(name = "artifact_version")
    private String artifactVersion = "0.0.1-SNAPSHOT";
    private String projectTitle;
    private String projectDesc;

    //rendered once per project
    private List<Template> application;
    //rendered once per concept
    private List<Template> projection;
}
