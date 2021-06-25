package com.konivax.utils;

import java.util.ArrayList;
import java.util.Collection;

public final class CollectionUtils {

    private CollectionUtils() { }

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean notEmpty(Collection<?> collection) {
        return (collection != null && !collection.isEmpty());
    }

    public static <T> ArrayList<T> arrayToList(final T... items) {
        ArrayList<T> list = new ArrayList<T>();
        for(T item : items)
            list.add(item);
        return list;
    }
}
