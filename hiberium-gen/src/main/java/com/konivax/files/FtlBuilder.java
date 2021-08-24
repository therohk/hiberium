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
    public static String renderFtlTemplate(final Map<String,Object> dataModel, String sourceBase, String basePath, Template template) {

        String templateName = template.getTemplate();
        if(!templateName.endsWith(".ftl"))
            return renderFileSimple(dataModel,
                    sourceBase, template.getSourcePath(), templateName,
                    basePath, template.getPackagePath(), template.getFilename());

        if(CollectionUtils.isEmpty(template.getDependencies()))
            return renderFtlTemplate(dataModel, templateName,
                    basePath, template.getPackagePath(), template.getFilename());

        Map<String,Object> localData = new HashMap<String,Object>();
        for(String dependency : template.getDependencies()) {
            String templateData = FtlUtils.parseNamedTemplate(dataModel, dependency);
            String embedName = Template.getTemplateEmbedName(dependency);
            localData.put(embedName, templateData);
        }
        localData.putAll(dataModel);

        return renderFtlTemplate(localData, templateName, basePath, template.getPackagePath(), template.getFilename());
    }

    /**
     * render freemarker template without dependencies
     * packagePath and fileName strings are evaluated as templates
     */
    public static String renderFtlTemplate(final Map<String,Object> dataModel, String templateName,
                                            String basePath, String packagePath, String fileName) {

        String packagePathRender = FtlUtils.parseLocalTemplate(dataModel, packagePath);
        String fileNameRender = FtlUtils.parseLocalTemplate(dataModel, fileName);

        String folderPath = FileUtils.getFilePath(basePath, packagePathRender);
        String filePath = FileUtils.getFilePath(basePath, packagePathRender, fileNameRender);

        FileUtils.createFolder(folderPath, true);
        FtlUtils.flushNamedTemplate(dataModel, templateName, filePath);

        Validate.isTrue(FileUtils.exists(filePath), "template render failed");
        return filePath;
    }

    /**
     * file is not processed as a template and just copied
     * suitable for non source code blob files
     */
    public static String renderFileSimple(final Map<String,Object> dataModel,
                                          String sourceBase, String sourcePackage, String templateName,
                                          String basePath, String packagePath, String fileName) {

        if(StringUtils.isBlank(packagePath))
            packagePath = sourcePackage;
        if(StringUtils.isBlank(fileName))
            fileName = templateName;

        String sourcePackageRender = FtlUtils.parseLocalTemplate(dataModel, sourcePackage);
        String packagePathRender = FtlUtils.parseLocalTemplate(dataModel, packagePath);
        String fileNameRender = FtlUtils.parseLocalTemplate(dataModel, fileName);

        String sourcePath = FileUtils.getFilePath(sourceBase, sourcePackageRender, templateName);
        String folderPath = FileUtils.getFilePath(basePath, packagePathRender);
        String filePath = FileUtils.getFilePath(basePath, packagePathRender, fileNameRender);

        Validate.isTrue(FileUtils.exists(sourcePath), "source file not found");

        FileUtils.createFolder(folderPath, true);
        FileUtils.copyFile(sourcePath, filePath, true);
        System.out.println("copied "+sourcePackage+"."+templateName+" -> "+filePath);

        Validate.isTrue(FileUtils.exists(filePath), "file copy failed");
        return filePath;
    }
}
