package com.konivax.models;

/**
 * base class for all hibernate entities
 */
public interface StoredObject<T> {

    Integer getPrimaryKey();

    void setPrimaryKey(Integer primaryKey);

    default void setPrimaryKeyNull() {
        setPrimaryKey(null);
    }

    String getPrimaryKeyFieldName();

    String getModelClassPath();

    /**
     * prepare eligible fields for new instance creation
     */
    void handleFieldsForInsert(Integer ownerId);

    /**
     * copy the field values that are eligible for update
     * default operations on some fields
     * @param update
     */
    void handleFieldsForUpdate(T update);

    /**
     * update eligible fields prior to soft delete operation
     */
    void handleFieldsForDelete();

    /**
     * merge object with update strategy code
     */
    void handleFieldsForMerge(T merge, String mergeCd);
}
