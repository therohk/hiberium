package com.konivax.utils.format;

import com.konivax.utils.StringUtils;
import com.konivax.utils.Validate;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.cache.TemplateLoader;
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

    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setOutputEncoding("UTF-8");

        try {
            configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20, soft:250");
        } catch (TemplateException te) {
            configuration.setCacheStorage(new NullCacheStorage());
            throw new RuntimeException("configuration creation failed", te);
        }

        MultiTemplateLoader mtl = null;
        ClassTemplateLoader ctl1 = new ClassTemplateLoader(FtlUtils.class, "/freemarker/");
        ClassTemplateLoader ctl2 = new ClassTemplateLoader(FtlUtils.class, "/springboot/");
        ClassTemplateLoader ctl3 = new ClassTemplateLoader(FtlUtils.class, "/projection/");
        ClassTemplateLoader ctl4 = new ClassTemplateLoader(FtlUtils.class, "/conception/");
//        FileTemplateLoader ftl2 = new FileTemplateLoader(new File("/templates/"));
        mtl = new MultiTemplateLoader(new TemplateLoader[]{ctl1, ctl2, ctl3, ctl4});
        configuration.setTemplateLoader(mtl);

        return configuration;
    }

    public static String parseNamedTemplate(final Map<String,Object> dataModel, String templateName) {
        StringWriter writer = new StringWriter();
        parseNamedTemplateToWriter(dataModel, templateName, writer);
        return writer.toString();
    }

    public static void parseNamedTemplateToWriter(final Map<String,Object> dataModel, String templateName, Writer writer) {
        Validate.isTrue(templateName.endsWith(".ftl"), "illegal template name");
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
        if(StringUtils.isEmpty(templateCode))
            return "";
        StringWriter writer = new StringWriter();
        parseLocalTemplateToWriter(dataModel, templateCode, writer);
        return writer.toString();
    }

    public static boolean parseLocalExpression(final Map<String,Object> dataModel, String expression, boolean defaultBool) {
        if(StringUtils.isBlank(expression))
            return defaultBool;
        String conditionCode = "<#if "+expression+">true<#else>false</#if>";
        String valueBool = parseLocalTemplate(dataModel, conditionCode);
        return Boolean.parseBoolean(valueBool);
    }

    public static String parseLocalTemplate(String templateCode
            , String label1, String value1
            , String label2, String value2
            , String label3, String value3) {
        Map<String,Object> dataModel = new HashMap<String,Object>();
        dataModel.put(label1, value1);
        dataModel.put(label2, value2);
        dataModel.put(label3, value3);
        return parseLocalTemplate(dataModel, templateCode);
    }

    public static void parseLocalTemplateToWriter(final Map<String,Object> dataModel, String templateCode, Writer writer) {
        Validate.notEmpty(templateCode, "template code not defined");

        //create template
        Template template;
        try {
            template = new Template("name", new StringReader(templateCode), getConfiguration());
        } catch (IOException ioe) {
            throw new RuntimeException("string template load failed", ioe);
        }

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
        Validate.notBlank(templateName,"template name not defined");
        Validate.notBlank(filePath,"target file path not defined");
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8);
            parseNamedTemplateToWriter(dataModel, templateName, writer);
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            throw new RuntimeException("template render failed: " + templateName, ioe);
        }
    }

    public static Set<String> getTemplateVariables(String templateName) {
        Template template = null;
        try {
            Configuration configuration = getConfiguration();
            configuration.setLogTemplateExceptions(false);
            template = configuration.getTemplate(templateName);
        } catch (IOException ioe) {
            throw new RuntimeException("file classpath template load failed: " + templateName, ioe);
        }
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> dataModel = new HashMap<String, Object>();
        boolean missingVar;
        do {
            missingVar = false;
            try {
                template.process(dataModel, stringWriter);
            } catch (InvalidReferenceException ire) {
                missingVar = true;
                dataModel.put(ire.getBlamedExpressionString(), "");
            } catch (IOException | TemplateException ioe) { }
        } while (missingVar);

        return dataModel.keySet();
    }

}
