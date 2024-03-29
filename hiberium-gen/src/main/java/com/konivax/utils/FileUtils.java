package com.konivax.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public final class FileUtils {

    private FileUtils() { }

    public static Path getPath(String file) {
        return Paths.get(file);
    }

    public static File getFile(String file) {
        return new File(file);
    }

    public static String getProjectBase() {
        String workingDir = System.getProperty("user.dir");
        if(workingDir.endsWith("/") || workingDir.endsWith("\\"))
            workingDir = workingDir.substring(0, workingDir.length()-1);
        String subProject = "hiberium-gen";
        if(workingDir.endsWith(subProject))
            workingDir = workingDir.substring(0, workingDir.length()-subProject.length()-1);
        if(!isFile(workingDir+"/settings.gradle"))
            throw new RuntimeException("wrong project base folder: "+workingDir);
        return workingDir;
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
        b.append(packagePath.replaceAll("[\\.]", "/"));
        if(b.length() != 0)
            b.append("/");
        return b.toString();
    }

    public static String getFilePath(String basePath, String packagePath, String fileName) {
        return getFilePath(basePath, packagePath) + fileName;
    }

    public static boolean exists(String location) {
        Path path = getPath(location);
        boolean ex = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
        boolean nex = Files.notExists(path, LinkOption.NOFOLLOW_LINKS);
        if((ex == true) && (nex == false))
            return true;
        return false;
    }

    public static boolean isFolder(String location) {
        return Files.isDirectory(getPath(location), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isFile(String location) {
        return Files.isRegularFile(getPath(location), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean createFolder(String location, boolean recursive) {
        if(exists(location))
            return false;
        new File(location).mkdirs();
        return true;
    }

    public static boolean copyFile(String source, String target, boolean overwrite) {
        if(!exists(source) || !isFile(source))
            return false;
        try {
            if(overwrite)
                Files.copy(getPath(source), getPath(target), StandardCopyOption.REPLACE_EXISTING);
            else
                Files.copy(getPath(source), getPath(target));
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public static List<String> findFilesRecursive(String dir) {
        List<String> files = null;
        try {
            files = Files.walk(Paths.get(dir))
                    .filter(Files::isRegularFile)
                    .map(p -> p.toString())
                    .collect(Collectors.toList());
        } catch (IOException ioe) {
            return null;
        }
        return files;
    }
}
