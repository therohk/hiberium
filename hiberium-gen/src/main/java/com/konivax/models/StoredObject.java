package com.konivax.models;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * base class for all hibernate entities
 */
public interface StoredObject<T> {

    Integer primaryKey();

    void primaryKey(Integer primaryKey);

    default void unsetPrimaryKey() {
        primaryKey(null);
    }

    Integer activeIndicator();

    void activeIndicator(Integer activeIndicator);

    Date lastUpdated();

    void lastUpdated(Date lastUpdated);

    String groupingKey();

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
//        handleFieldsForUpdate(merge);
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields)
            handleFieldForMerge(merge, field, mergeCd);
    }

    default void handleFieldForMerge(T merge, Field field, String mergeFlag) {
        switch (mergeFlag) {

        }
    }

}
