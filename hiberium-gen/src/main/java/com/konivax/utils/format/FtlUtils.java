package com.konivax.utils.format;

import com.konivax.utils.StringUtils;
import freemarker.cache.*;
import freemarker.core.InvalidReferenceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class FtlUtils {

    private FtlUtils() { }

    public static String getTemplateBaseName(String templateName) {
        if(templateName.endsWith(".ftl"))
            return templateName.substring(0, templateName.length()-4);
        return templateName;
    }

    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setOutputEncoding("UTF-8");

        try {
            configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20, soft:250");
        } catch (TemplateException te) {
            configuration.setCacheStorage(new NullCacheStorage());
//            te.printStackTrace();
            throw new RuntimeException("configuration creation failed", te);
        }
//        String basePath = Settings.getSetting("freemarker.render.config", "/freemarker/");
        MultiTemplateLoader mtl = null;
//        try {
//            ClassTemplateLoader ctl = new ClassTemplateLoader(FtlUtils.class, "/templates/");
//            FileTemplateLoader ftl2 = new FileTemplateLoader(new File("/templates/"));
//            mtl = new MultiTemplateLoader(new TemplateLoader[]{ctl, ftl2});
//
//        } catch (IOException ioe) {
//            throw new ParseException("configuration creation failed", ioe);
//        }

        ClassTemplateLoader ctl = new ClassTemplateLoader(FtlUtils.class, "/freemarker/");
        mtl = new MultiTemplateLoader(new TemplateLoader[]{ctl});
        configuration.setTemplateLoader(mtl);

        return configuration;
    }

    public static String parseNamedTemplate(final Map<String,Object> dataModel, String templateName) {
        StringWriter writer = new StringWriter();
        parseNamedTemplateToWriter(dataModel, templateName, writer);
        return writer.toString();
    }

    public static void parseNamedTemplateToWriter(final Map<String,Object> dataModel, String templateName, Writer writer) {
//        Validate.isTrue(templateName.endsWith(".ftl"), "illegal template name");
        Configuration configuration = getConfiguration();

        //load template
        Template template;
        try {
            template = configuration.getTemplate(templateName);
        } catch (IOException ioe) {
            throw new RuntimeException("file classpath template load failed: " + templateName, ioe);
        }

        //render template
        try {
            template.process(dataModel, writer);
        } catch (IOException ioe) {
            throw new RuntimeException("template render failed: " + templateName, ioe);
        } catch (TemplateException te) {
            throw new RuntimeException("template render failed: " + templateName, te);
        }
    }

    public static String parseLocalTemplate(final Map<String,Object> dataModel, String templateCode) {
        StringWriter writer = new StringWriter();
        parseLocalTemplateToWriter(dataModel, templateCode, writer);
        return writer.toString();
    }

    public static void parseLocalTemplateToWriter(final Map<String,Object> dataModel, String templateCode, Writer writer) {
        if(StringUtils.isEmpty(templateCode))
            throw new IllegalArgumentException("template code not defined");

        //create template
        Template template;
        try {
            template = new Template("name", new StringReader(templateCode), getConfiguration());
        } catch (IOException ioe) {
            throw new RuntimeException("string template load failed", ioe);
        }
        //for debug
//        Writer writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
//        template.process(data, writer);

        //render template
        try {
            template.process(dataModel, writer);
        } catch (IOException ioe) {
            throw new RuntimeException("string template load failed", ioe);
        } catch (TemplateException te) {
            throw new RuntimeException("string template load failed", te);
        }
    }

    public static void flushNamedTemplate(final Map<String,Object> dataModel, String templateName, String filePath) {
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8);
            parseNamedTemplateToWriter(dataModel, templateName, writer);
            writer.flush();
            writer.close();
            System.out.println("written "+templateName+" to "+filePath);
        } catch (IOException ioe) {
            throw new RuntimeException("template render failed: " + templateName, ioe);
        }
    }

    /**
     * todo avoid printing logs for this function
     */
    public static Set<String> getTemplateVariables(String templateName) {
        Template template = null;
        try {
            template = getConfiguration().getTemplate(templateName);
        } catch (IOException ioe) {
            throw new RuntimeException("file classpath template load failed: " + templateName, ioe);
        }
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> dataModel = new HashMap<>();
        boolean exceptionCaught;

        do {
            exceptionCaught = false;
            try {
                template.process(dataModel, stringWriter);
            } catch (InvalidReferenceException e) {
                exceptionCaught = true;
                dataModel.put(e.getBlamedExpressionString(), "");
            } catch (IOException | TemplateException e) {
//                throw new IllegalStateException("Failed to Load Template: " + templateName, e);
            }
        } while (exceptionCaught);

        return dataModel.keySet();
    }

}
