package com.konivax.models;

/**
 * base class for all hibernate entities
 */
public interface StoredObject<T> {

    Integer getPrimaryKey();

    void setPrimaryKey(Integer primaryKey);

    default void unsetPrimaryKey() {
        setPrimaryKey(null);
    }

    String getPrimaryKeyFieldName();

//    String getModelClassPath();

    /**
     * prepare eligible fields for new instance creation
     */
    void handleFieldsForInsert(Integer ownerId);

    /**
     * copy eligible fields for update into current instance
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
    default void handleFieldsForMerge(T merge, String mergeCd) {
        handleFieldsForUpdate(merge);
    }
}
