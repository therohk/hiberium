package com.konivax.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class CollectionUtils {

    private CollectionUtils() { }

    public static boolean isEmpty(final Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean notEmpty(final Collection<?> collection) {
        return (collection != null && !collection.isEmpty());
    }

    public static <T> boolean in(final T needle, final Collection<T> haystack) {
        if(haystack == null || haystack.size() == 0)
            return false;
        return haystack.contains(needle);
    }

    public static boolean has(final String string, final Collection<String> searches) {
        if(string == null || string.length() == 0)
            return false;
        if(searches == null || searches.size() == 0)
            return false;
        for(String search : searches) {
            if(string.contains(search))
                return true;
        }
        return false;
    }

    public static List<?> getLimitOffset(final Collection<?> collection, int page, int size) {
        if(page <= 0)
            page = 1;
        if(size <= 0)
            size = 20;
        return collection.stream()
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

}
