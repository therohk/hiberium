package com.konivax.models;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * same as freemarker template stored-object-java.ftl
 */
public interface StoredObject<T> {

    //common entity reflections

    default String getEntityName() {
        return this.getClass().getSimpleName();
    }

    default String getEntityClassPath() {
        return this.getClass().getName();
    }

    default String getTableName() {
        Table tableAnnotation = this.getClass().getAnnotation(Table.class);
        if(tableAnnotation == null)
            return null;
        String table = tableAnnotation.name();
        String schema = tableAnnotation.schema();
        return schema.isBlank() ? table : schema+"."+table;
    }

    default List<String> getFieldNamesAsList() {
        Field[] fields = this.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .map(f -> f.getName())
                .collect(Collectors.toList());
    }

    default List<String> getColumnNamesAsList() {
        Field[] fields = this.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> f.getAnnotation(Column.class) != null)
                .map(f -> f.getAnnotation(Column.class).name())
                .collect(Collectors.toList());
    }

}
