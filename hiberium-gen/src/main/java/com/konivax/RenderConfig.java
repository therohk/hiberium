package com.konivax;

import com.konivax.utils.FileUtils;
import com.konivax.utils.Validate;
import org.apache.commons.cli.*;

public class RenderConfig {

    protected Options options = new Options();
    protected CommandLineParser parser = new PosixParser();
    protected CommandLine cmd = null;

    public static void main(String[] args) {

        RenderConfig render = new RenderConfig();
        render.configure();
        render.parse(args);

        String projectPath = FileUtils.getCurrentPath("hiberium");
        String configPath = projectPath+"\\hiberium-gen\\src\\main\\resources\\";
        String projectYaml = null;
        String conceptCsv = null;
        String attributeCsv = null;

        if(render.hasOption("h")) {
            String usage = RenderConfig.class.getName() + " -p project.yaml -c concept.csv -a attribute.csv";
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(usage, render.options);
            System.exit(0);
        }

        projectYaml = render.getOption("p", configPath+"hibernate-render.yaml");
        conceptCsv = render.getOption("c", configPath+"concept-def.csv");
        attributeCsv = render.getOption("a", configPath+"attribute-xref.csv");

        RenderProject.renderFromConfig(projectPath, projectYaml, conceptCsv, attributeCsv);
    }

    protected void configure() {
        option("h", "help",      false, false, 0, "print help menu");
        option("p", "project",   false, true,  1, "load project yaml file from path");
        option("c", "concept",   false, true,  1, "load concepts csv file from path");
        option("a", "attribute", false, true,  1, "load attributes csv file from path");
    }

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
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("-c concept.csv -a attribute.csv -p project.yaml", options);
        }
    }

    protected boolean hasOption(String shortCmd) {
        return cmd.hasOption(shortCmd);
    }

    protected String getOption(String shortCmd, String defaultVal) {
        return cmd.getOptionValue(shortCmd, defaultVal);
    }

}
