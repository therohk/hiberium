package com.konivax.utils;

import javax.persistence.Column;
import javax.persistence.Id;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
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

    public static <T> String[] getColumnNamesAsArray(Class<T> clazz) {
        List<String> columnNames = getColumnNamesAsList(clazz);
        return columnNames.toArray((String[]) Array.newInstance(String.class, columnNames.size()));
    }

    public static Map<String,Object> toFieldObjectMap(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        Map<String, Object> objectMap = new HashMap<String, Object>();
        for (Field field : fields) {
            Object obj = runGetter(field, o);
            if(obj == null)
                continue;
            objectMap.put(field.getName(), obj);
        }
        return objectMap;
    }

    /**
     * export all fields containing column annotation
     */
    public static Map<String,Object> toColumnObjectMap(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        Map<String, Object> objectMap = new HashMap<String, Object>();
        for (Field field : fields) {
            if(field.getAnnotation(Column.class) == null)
                continue;
            Object obj = runGetter(field, o);
            if(obj == null)
                continue;
            objectMap.put(field.getAnnotation(Column.class).name(), obj);
        }
        return objectMap;
    }

    public static <T> Field getEntityPrimaryKey(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Id IdAnnotation = field.getAnnotation(Id.class);
            if (IdAnnotation != null)
                return field;
        }
        return null;
    }

    public static <T> Field getEntityFieldByName(Class<T> clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName))
                return field;
            if(field.getAnnotation(Column.class) != null)
                if(field.getAnnotation(Column.class).name().equals(fieldName))
                    return field;
        }
        return null;
    }

    public static Object runGetter(Field field, Object obj) {
        for (Method method : obj.getClass().getMethods()) {
            if ((method.getName().startsWith("get"))
                    && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        return method.invoke(obj);
                    } catch (IllegalAccessException iae) {
                        System.err.println("Could not determine method: " + method.getName());
                    } catch (InvocationTargetException ite) {
                        System.err.println("Could not determine method: " + method.getName());
                    }
                }
            }
        }
        return null;
    }

    public static Object invokeGetter(Object obj, String fieldName) {
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            Method getter = pd.getReadMethod();
            try {
                return getter.invoke(obj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException iae) {
                throw new RuntimeException(iae);
            }
        } catch (IntrospectionException ie) {
            throw new RuntimeException(ie);
        }
    }

    public static void invokeSetter(Object obj, String fieldName, Object targetValue) {
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            Method setter = pd.getWriteMethod();
            try {
                setter.invoke(obj, targetValue);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException iae) {
//                e.printStackTrace();
                throw new RuntimeException(iae);
            }
        } catch (IntrospectionException ie) {
            throw new RuntimeException(ie);
        }
    }
}
