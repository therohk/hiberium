package ${package_base}.models;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * base class for all database entities
 * avoid functions starting with get or set
 */
public interface StoredObject<T> {

    T fetchEntity();

    default String fetchEntityName() {
        return this.getClass().getSimpleName();
    }

    default String fetchEntityClassPath() {
        return this.getClass().getName();
    }

    default String fetchTableName() {
        Table tableAnnotation = this.getClass().getAnnotation(Table.class);
        if(tableAnnotation == null)
            return null;
        String table = tableAnnotation.name();
        String schema = tableAnnotation.schema();
        return schema.isBlank() ? table : schema+"."+table;
    }

    default List<String> fetchFieldNamesAsList() {
        Field[] fields = this.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .map(f -> f.getName())
                .collect(Collectors.toList());
    }

    default List<String> fetchColumnNamesAsList() {
        Field[] fields = this.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> f.getAnnotation(Column.class) != null)
                .map(f -> f.getAnnotation(Column.class).name())
                .collect(Collectors.toList());
    }

}