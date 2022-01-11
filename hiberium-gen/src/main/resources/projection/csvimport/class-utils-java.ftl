package ${package_base}.utils;

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

    public static Class<?> findClass(String className, String underPackage)
            throws ClassNotFoundException {
        try {
            List<Class<?>> clazzes = getClassesForPackage(underPackage);
            for (Class<?> clazz : clazzes)
                if (className.equals(clazz.getSimpleName()))
                    return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        throw new ClassNotFoundException("class not found for "+className);
    }

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
