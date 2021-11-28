package com.konivax.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtils {

    private ClassUtils() { }

    public static Class<?> findClass(String className, String underPackage) {
        try {
            List<Class<?>> clazzes = getClassesForPackage(underPackage);
            for (Class<?> clazz : clazzes)
                if (className.equals(clazz.getSimpleName()))
                    return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * scans all classes accessible from the context class loader
     * which belong to the given package and subpackages
     * @param packageName The base package
     * @return the classes
     * @deprecated does not work with jar files
     */
    @Deprecated
    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * recursive method used to find all classes within a given directory
     * @param directory   the base directory
     * @param packageName the package name for classes found inside the base directory
     * @return the classes
     */
    @Deprecated
    public static List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    //alternate method, proven to work on deployed servers

    /**
     * attempts to list all the classes in the specified package
     * as determined by the context class loader
     * @param packageName the package name to search
     * @return a list of classes that exist within that package
     * @throws ClassNotFoundException if something went wrong
     */
    public static ArrayList<Class<?>> getClassesForPackage(String packageName)
            throws ClassNotFoundException {

        final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        try {
            final ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null)
                throw new ClassNotFoundException("class loader not found");
            final Enumeration<URL> resources = cld.getResources(packageName.replace('.', '/'));

            URLConnection connection;
            for (URL url = null; resources.hasMoreElements() && ((url = resources.nextElement()) != null); ) {
                try {
                    connection = url.openConnection();
                    if (connection instanceof JarURLConnection) {
                        checkJarFile((JarURLConnection) connection, packageName, classes);
                    } else {
                        try {
                            checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), packageName, classes);
                        } catch (final UnsupportedEncodingException ex) {
                            throw new ClassNotFoundException(packageName + " does not appear to be a valid package", ex);
                        }
                    }
                } catch (final IOException ioex) {
                    throw new ClassNotFoundException("failed to get all resources for " + packageName, ioex);
                }
            }
        } catch (final NullPointerException ex) {
            throw new ClassNotFoundException(packageName + " does not appear to be a valid package", ex);
        } catch (final IOException ioex) {
            throw new ClassNotFoundException("failed get all resources for " + packageName, ioex);
        }
        return classes;
    }

    /**
     * @param directory the directory to start with
     * @param packageName package name to search for
     * @param classes if a file is not loaded but still is in the directory
     * @throws ClassNotFoundException
     */
    private static void checkDirectory(File directory, String packageName, ArrayList<Class<?>> classes)
            throws ClassNotFoundException {

        File tmpDirectory;
        if (directory.exists() && directory.isDirectory()) {
            final String[] files = directory.list();
            for (final String file : files) {
                if (file.endsWith(".class")) {
                    try {
                        classes.add(Class.forName(packageName + '.' + file.substring(0, file.length() - 6)));
                    } catch (final NoClassDefFoundError e) {
                        // do nothing. this class has not been found by the loader
                    }
                } else if ((tmpDirectory = new File(directory, file)).isDirectory()) {
                    checkDirectory(tmpDirectory, packageName + "." + file, classes);
                }
            }
        }
    }

    /**
     * @param connection the connection to the jar
     * @param packageName the package name to search for
     * @param classes the current ArrayList to add new classes
     * @throws ClassNotFoundException if a file is not loaded but still is in the jar file
     * @throws IOException if it cannot correctly read from the jar file
     */
    private static void checkJarFile(JarURLConnection connection, String packageName, ArrayList<Class<?>> classes)
            throws ClassNotFoundException, IOException {

        final JarFile jarFile = connection.getJarFile();
        final Enumeration<JarEntry> entries = jarFile.entries();

        String name;
        for (JarEntry jarEntry = null; entries.hasMoreElements() && ((jarEntry = entries.nextElement()) != null); ) {
            name = jarEntry.getName();
            if (name.contains(".class")) {
                name = name.substring(0, name.length() - 6).replace('/', '.');
                if (name.contains(packageName)) {
                    classes.add(Class.forName(name));
                }
            }
        }
    }

}
