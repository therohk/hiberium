package com.konivax.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Project {

    private String projectType;
    private String projectName;
    private String packageBase;
    private String contextBase;

    private String artifactVersion = "0.0.1-SNAPSHOT";
    private String projectTitle;
    private String projectDesc;

    //rendered once per project
    private List<Template> application;
    //rendered once per concept
    private List<Template> projection;
}
