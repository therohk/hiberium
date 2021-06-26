package com.konivax.utils;

import javax.persistence.Column;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ReflectUtils {

    private ReflectUtils() { }

    public static <T> List<String> getFieldNamesAsList(Class<T> clazz) {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    public static <T> String[] getFieldNamesAsArray(Class<T> clazz) {
        List<String> fields = getFieldNamesAsList(clazz);
        return fields.toArray((String[]) Array.newInstance(String.class, fields.size()));
    }

    public static <T> String[] getFieldNamesAsArray(Class<T> clazz, String[] headers) {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();

        for(String header : headers) {
            for(Field field : fields) {
                if(field.getAnnotation(Column.class) == null)
                    continue;
                if(header.equals(field.getAnnotation(Column.class).name()))
                    fieldNames.add(field.getName());
            }
        }
        if(headers.length != fieldNames.size())
            throw new RuntimeException("fields not available or mismatched");
        return fieldNames.toArray((String[]) Array.newInstance(String.class, fieldNames.size()));
    }

    public static <T> List<String> getColumnNamesAsList(Class<T> clazz) {
        List<String> columnNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null)
                columnNames.add(column.name());
        }
        return columnNames;
    }

    public static Map<String,Object> toFieldObjectMap(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        Map<String, Object> objectMap = new HashMap<String, Object>();
        for (Field field : fields) {
            Object obj = runGetter(field, o);
            objectMap.put(field.getName(), obj);
        }
        return objectMap;
    }

    public static Map<String,Object> toColumnObjectMap(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        Map<String, Object> objectMap = new HashMap<String, Object>();
        for (Field field : fields) {
            if(field.getAnnotation(Column.class) == null)
                continue;
            Object obj = runGetter(field, o);
            objectMap.put(field.getAnnotation(Column.class).name(), obj);
        }
        return objectMap;
    }

    public static Object runGetter(Field field, Object o) {
        for (Method method : o.getClass().getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        return method.invoke(o);
                    } catch (IllegalAccessException e) {
                        System.err.println("Could not determine method: " + method.getName());
                    } catch (InvocationTargetException e) {
                        System.err.println("Could not determine method: " + method.getName());
                    }
                }
            }
        }
        return null;
    }
}
