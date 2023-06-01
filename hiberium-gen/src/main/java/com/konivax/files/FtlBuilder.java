package com.konivax.files;

import com.konivax.models.Template;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.FileUtils;
import com.konivax.utils.StringUtils;
import com.konivax.utils.Validate;
import com.konivax.utils.format.FtlUtils;

import java.util.HashMap;
import java.util.Map;

public final class FtlBuilder {

    private FtlBuilder() { }

    /**
     * render freemarker template with dependencies
     * dependant templates are rendered before the base template
     * their outputs are added back to the data map variable
     * they are accessed and injected as text by the base template
     */
    public static String renderFtlTemplate(final Map<String,Object> dataModel,
                                           String sourceBase, String targetBase, Template template) {

        String templateName = template.getTemplate();
        if(!FtlUtils.parseLocalExpression(dataModel, template.getCondition(), true)) {
            System.out.println("skipped "+templateName+" as per condition");
            return null;
        }

        if(!templateName.endsWith(".ftl")) {
            return renderFileSimple(dataModel,
                    sourceBase, template.getSourcePath(), templateName,
                    targetBase, template.getPackagePath(), template.getFilename());
        }

        Map<String,Object> localData = dataModel;
        if(CollectionUtils.notEmpty(template.getDependencies())) {
            localData = new HashMap<String,Object>();
            for (String dependency : template.getDependencies()) {
                String templateData = FtlUtils.parseNamedTemplate(dataModel, dependency);
                String embedName = Template.getTemplateEmbedName(dependency);
                localData.put(embedName, templateData);
            }
            localData.putAll(dataModel);
        }

        return renderFtlTemplate(localData, templateName,
                targetBase, template.getPackagePath(), template.getFilename());
    }

    /**
     * render freemarker template without dependencies
     * todo handle templates not under default path
     */
    public static String renderFtlTemplate(final Map<String,Object> dataModel, String templateName,
                                            String targetBase, String packagePath, String fileName) {

        String folderPath = renderFilePath(dataModel, targetBase, packagePath, null);
        String filePath = renderFilePath(dataModel, targetBase, packagePath, fileName);

        FileUtils.createFolder(folderPath, true);
        FtlUtils.flushNamedTemplate(dataModel, templateName, filePath);

        Validate.isTrue(FileUtils.exists(filePath), "template render failed");
        System.out.println("rendered "+templateName+" -> "+filePath);
        return filePath;
    }

    /**
     * file is not processed as a template and just copied
     * suitable for non source code blob files
     */
    public static String renderFileSimple(final Map<String,Object> dataModel,
                                          String sourceBase, String sourcePackage, String templateName,
                                          String targetBase, String targetPackage, String fileName) {

        if(StringUtils.isBlank(targetPackage))
            targetPackage = sourcePackage;
        if(StringUtils.isBlank(fileName))
            fileName = templateName;

        String sourcePath = renderFilePath(dataModel, sourceBase, sourcePackage, templateName);
        String folderPath = renderFilePath(dataModel, targetBase, targetPackage, null);
        String filePath = renderFilePath(dataModel, targetBase, targetPackage, fileName);

        Validate.isTrue(FileUtils.exists(sourcePath), "source file not found "+sourcePath);

        FileUtils.createFolder(folderPath, true);
        FileUtils.copyFile(sourcePath, filePath, true);

        Validate.isTrue(FileUtils.exists(filePath), "file copy failed");
        System.out.println("copied "+sourcePackage+"."+templateName+" -> "+filePath);
        return filePath;
    }

    /**
     * generate file path based on given parameters
     * packagePath and fileName strings are evaluated as templates
     */
    public static String renderFilePath(final Map<String,Object> dataModel,
                                        String basePath, String packagePath, String fileName) {

        String packagePathRender = FtlUtils.parseLocalTemplate(dataModel, packagePath);
        if(StringUtils.isBlank(fileName))
            return FileUtils.getFilePath(basePath, packagePathRender);
        String fileNameRender = FtlUtils.parseLocalTemplate(dataModel, fileName);
        return FileUtils.getFilePath(basePath, packagePathRender, fileNameRender);
    }
}
