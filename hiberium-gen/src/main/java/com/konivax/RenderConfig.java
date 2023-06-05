package com.konivax;

import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.models.Project;
import com.konivax.models.mapper.ModelValidator;
import com.konivax.utils.FileUtils;
import com.konivax.utils.StringUtils;
import com.konivax.utils.Validate;
import com.konivax.utils.format.CsvUtils;
import com.konivax.utils.format.YamlUtils;
import org.apache.commons.cli.*;

import java.util.List;

/**
 * config loaded from java args or env variables
 * suitable for pipelining builds
 */
public class RenderConfig {

    protected Options options = new Options();
    protected CommandLineParser parser = new PosixParser();
    protected CommandLine cmd = null;

    public static void main(String[] args) {

        RenderConfig render = new RenderConfig();
        render.configure();
        render.parse(args);

        String projectPath = FileUtils.getProjectBase();
        String configPath = projectPath+"/hiberium-gen/src/main/resources/";
        System.out.println("project path: "+projectPath);

        if(render.hasOption("h")) {
            render.printHelpExit(0);
        }

        //check config files
        String sourcePath = render.getOption("s", configPath);
        System.out.println("source path: "+sourcePath);
        String projectYaml = render.getOption("p", "PROJECT_FILE", sourcePath+"hibernate-render.yaml");
        String conceptCsv = render.getOption("c", "CONCEPT_FILE", sourcePath+"concept-def.csv");
        String attributeCsv = render.getOption("a", "ATTRIBUTE_FILE", sourcePath+"attribute-xref.csv");

        Validate.isTrue(FileUtils.exists(projectYaml), "project yaml not found: "+projectYaml);
        Validate.isTrue(FileUtils.exists(conceptCsv), "concept csv not found: "+conceptCsv);
        Validate.isTrue(FileUtils.exists(attributeCsv), "attribute csv not found: "+attributeCsv);

        //load data model
        Project project = YamlUtils.deserializeFile(projectYaml, Project.class);
        project.setProjectName(render.getOption("n", project.getProjectName()));
        project.setArtifactVersion(render.getOption("v", project.getArtifactVersion()));

        List<Concept> conceptList = CsvUtils.readCsvFileData(conceptCsv, Concept.class);
        List<Attribute> attributeList = CsvUtils.readCsvFileData(attributeCsv, Attribute.class);

        //build and validate
        ModelValidator.validateProject(project);
        RenderProject.attachConceptAttributes(conceptList, attributeList);
        RenderProject.attachConceptRelations(conceptList, attributeList);
        ModelValidator.validateModel(conceptList);

        //code source and target
        String targetDefault = projectPath + "/" + project.getProjectName() + "/";
        String targetPath = render.getOption("t", targetDefault);
        System.out.println("target path: "+targetPath);

        RenderProject.renderFromModel(sourcePath, targetPath, project, conceptList);
    }

    protected void configure() {
        option("h", "help",      false, false, 0, "print help menu");
        option("p", "project",   false, true,  1, "load project yaml file from path");
        option("c", "concept",   false, true,  1, "load concepts csv file from path");
        option("a", "attribute", false, true,  1, "load attributes csv file from path");
        option("s", "sourcePath",     false, true,  1, "source path for config files");
        option("t", "targetPath",     false, true,  1, "target path for rendered project");
        option("n", "projectName",    false, true,  1, "override output project name");
        option("v", "artifactVersion",false, true,  1, "override output artifact version");
//        option("f", "templatePath",   false, true,  1, "source path for additional templates");
    }

    //-------------------------------------------------------------------------

    protected void option(String shortCmd, String longCmd, boolean isReq, boolean hasArg, int countArg, String desc) {
        Option option = new Option(shortCmd, longCmd, hasArg, desc);
        if(hasArg)
            option.setArgs(countArg);
        option.setRequired(isReq);
        option.setType(String.class);
        option.setValueSeparator(',');
        Validate.isFalse(options.hasOption(shortCmd), "option already configured: " + shortCmd);
        options.addOption(option);
    }

    protected void parse(String[] args) {
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            pe.printStackTrace();
            printHelpExit(2);
        }
    }

    protected void printHelpExit(int exit) {
        String usage = RenderConfig.class.getName() + " -p project.yaml -c concept.csv -a attribute.csv";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(usage, options);
        if(exit >= 0)
            System.exit(exit);
    }

    protected boolean hasOption(String shortCmd) {
        return cmd.hasOption(shortCmd);
    }

    protected String getOption(String shortCmd, String defValue) {
        return cmd.getOptionValue(shortCmd, defValue);
    }

    protected String getOption(String shortCmd, String varName, String defValue) {
        String value = cmd.getOptionValue(shortCmd);
        if(StringUtils.notBlank(value))
            return value;
        return getEnvVariable(varName, defValue);
    }

    protected String getEnvVariable(String varName, String defValue) {
        String value = System.getenv(varName);
        return StringUtils.isBlank(value) ? defValue : value;
    }
}
