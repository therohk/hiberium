package com.konivax.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtils {

    private FileUtils() { }

    public static Path getPath(String file) {
        return Paths.get(file);
    }

    public static File getFile(String file) {
        return new File(file);
    }

    public static String getCurrentPath(String projectName) {
        String workingDir = System.getProperty("user.dir");
        String projectPath = workingDir.substring(0, workingDir.indexOf(projectName)+projectName.length());
        return projectPath;
    }

    public static String getPackagePath(String basePackage, String subPackage, String moduleName) {
        StringBuilder b = new StringBuilder();
        b.append(basePackage);
        b.append(".").append(subPackage);
        if(StringUtils.notEmpty(moduleName))
            b.append(".").append(moduleName);
        return b.toString();
    }

    public static String getFilePath(String basePath, String packagePath) {
        StringBuilder b = new StringBuilder();
        b.append(basePath);
        b.append(packagePath.replaceAll("[\\.]", "\\\\"));
        b.append("\\\\");
        return b.toString();
    }

    public static String getFilePath(String basePath, String packagePath, String fileName) {
        StringBuilder b = new StringBuilder();
        b.append(basePath);
        b.append(packagePath.replaceAll("[\\.]", "\\\\"));
        b.append("\\\\");
        b.append(fileName);
        return b.toString();
    }

    public static boolean exists(String location) {
        Path path = getPath(location);
        boolean ex = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
        boolean nex = Files.notExists(path, LinkOption.NOFOLLOW_LINKS);
        if ((ex == true ) && (nex == false))
            return true;
        return false;
    }

    public static boolean createFolder(String location, boolean recursive) {
        if(exists(location)) {
            return false;
        }
        new File(location).mkdirs();
        return true;
    }

}
