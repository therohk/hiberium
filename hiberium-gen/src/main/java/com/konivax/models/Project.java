package com.konivax.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

/**
 * @author therohk 2021/06/25
 */
@Getter
@Setter
public class Project {

    private String projectType;
    private String projectName;
    private String projectTitle;
    private String projectDesc;

    @Column(name = "package_base")
    private String packageBase;
    @Column(name = "context_base")
    private String contextBase;
    @Column(name = "artifact_version")
    private String artifactVersion;

    //rendered once per project
    private List<Template> composition;
    //rendered once per concept
    private List<Template> projection;
}
