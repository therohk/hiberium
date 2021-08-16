package com.konivax.files;

import com.konivax.models.Template;
import com.konivax.utils.CollectionUtils;
import com.konivax.utils.FileUtils;
import com.konivax.utils.Validate;
import com.konivax.utils.format.FtlUtils;

import java.util.HashMap;
import java.util.Map;

public final class FtlBuilder {

    /**
     * render freemarker template with dependencies
     */
    public static String renderFtlTemplate(final Map<String,Object> dataModel, String basePath, Template template) {

        if(CollectionUtils.isEmpty(template.getDependencies()))
            return renderFtlTemplate(dataModel, template.getTemplate(), basePath, template.getPackagePath(), template.getFilename());

        String packagePath = FtlUtils.parseLocalTemplate(dataModel, template.getPackagePath());
        String fileName = FtlUtils.parseLocalTemplate(dataModel, template.getFilename());

        Map<String,Object> localData = new HashMap<String,Object>();
        for(String dependency : template.getDependencies()) {
            String templateName = FtlUtils.getTemplateBaseName(dependency);
            String templateData = FtlUtils.parseNamedTemplate(dataModel, dependency);

            String embedName = "render_"+templateName.replaceAll("[\\-]", "_");
            localData.put(embedName, templateData);
        }
        localData.putAll(dataModel);

        String folderPath = FileUtils.getFilePath(basePath, packagePath);
        String filePath = FileUtils.getFilePath(basePath, packagePath, fileName);

        FileUtils.createFolder(folderPath, true);
        FtlUtils.flushNamedTemplate(localData, template.getTemplate(), filePath);

        Validate.isTrue(FileUtils.exists(filePath), "template render failed");
        return filePath;
    }

    /**
     * render freemarker template without dependencies
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
}