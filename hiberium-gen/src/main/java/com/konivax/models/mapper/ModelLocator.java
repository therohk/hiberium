package com.konivax.models.mapper;

import com.konivax.utils.Validate;

import java.util.Random;

/**
 * assign a point on the surface of a sphere
 */
public final class ModelLocator {

    private ModelLocator() { }

    public static double[] locateObject(Object obj) {
        Validate.notNull(obj, "object is null");
        Random random = new Random(obj.hashCode());
        final int laMin = -90, laMax = 90, loMin = -180, loMax = 180;
        double lon = (random.nextDouble()*(loMax - loMin)) + loMin;
        double lat = (random.nextDouble()*(laMax - laMin)) + laMin;
        return new double[] {lon, lat};
    }

    public static double[] locateClass(Class<?> clazz) {
        Validate.notNull(clazz, "class is null");
        return locateObject(clazz.getName());
    }
}
