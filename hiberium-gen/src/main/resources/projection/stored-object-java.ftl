package ${package_base}.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    String apiPath();

    Integer primaryKey();

    void primaryKey(Integer primaryKey);

    default void unsetPrimaryKey() {
        primaryKey(null);
    }

    /**
     * prepare eligible fields for new instance creation
     * invoked prior to an insert operation
     */
    void handleFieldsForInsert();

    /**
     * prepare eligible fields for instance inactivation
     * invoked prior to a soft delete operation
     */
    void handleFieldsForDelete();

    /**
     * copy eligible fields from source into instance
     * default operations possible on some fields
     * id and immutable fields should never be changed
     * same effect as strategy Y
     */
    void handleFieldsForUpdate(T source);

    /**
     * merge eligible fields from source into instance
     * provided strategy code may be ignored by field
     * override to configure separate strategy per field
     */
    default void handleFieldsForMerge(T source, String strategy) {
        if(source == null || "N".equals(strategy))
            return;
        if(strategy == null) {
            handleFieldsForUpdate(source);
            return;
        }
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields)
            handleFieldForMerge(fetchEntity(), source, field.getName(), strategy);
    }

    default void handleFieldForMerge(T source, String fieldName, String strategy) {
        MergeObject.handleFieldForMerge(fetchEntity(), source, fieldName, strategy);
    }

    default void handleFieldForMerge(T target, T source, String fieldName, String strategy) {
        MergeObject.handleFieldForMerge(target, source, fieldName, strategy);
    }

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

    default Object invokeGetter(String fieldName) {
        return MergeObject.invokeGetter(this, fieldName);
    }

    default void invokeSetter(String fieldName, Object value) {
        MergeObject.invokeSetter(this, fieldName, value);
    }

    default String invokeToString() {
        try {
            return new ObjectMapper().writeValueAsString(fetchEntity());
        } catch (JsonProcessingException jpe) {
            return getClass().getName() + "@" + Integer.toHexString(hashCode());
        }
    }
}