package com.konivax.models.merge;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * supports same update strategy values
 * works for data type HashMap instead of StoredObject
 */
public final class MergeHashMap {

    private MergeHashMap() { }

    /**
     * same logic but data type is a map of objects
     */
    public static void handleFieldForMerge(Map<String,Object> target, final Map<String,Object> source, String mapKey, String strategy) {
        assert target != null : "target object is null";
        assert source != null : "source object is null";
        assert mapKey != null : "field name not defined";
        assert strategy.length() == 1 : "strategy code is invalid";

        Object targetVal = target.get(mapKey);
        Object sourceVal = source.get(mapKey);
        boolean hasTargetVal = targetVal != null;
        boolean hasSourceVal = sourceVal != null;
        if(!hasTargetVal && !hasSourceVal)
            return;
        if(hasTargetVal && targetVal.equals(sourceVal))
            return;

        if(MergeObject.approveTargetSetter(strategy, hasTargetVal, hasSourceVal))
            target.put(mapKey, sourceVal);
    }

    /**
     * override merge flag for all fields
     */
    public static Map<String,Object> handleFieldsForMerge(Map<String,Object> target, final Map<String,Object> source, String strategy) {
        if("N".equals(strategy) || source == null)
            return target;
        if("C".equals(strategy))
            return source;
        if(strategy == null) {
            target.putAll(source);
            return target;
        }

        Set<String> mapKeyList = new LinkedHashSet<String>();
        mapKeyList.addAll(target.keySet());
        mapKeyList.addAll(source.keySet());
        for(String mapKey : mapKeyList)
            handleFieldForMerge(target, source, mapKey, strategy);
        return target;
    }

    /**
     * merge fields into a new map instance
     */
    public static Map<String,Object> handleFieldsForMergeInto(final Map<String,Object> target, final Map<String,Object> source, String strategy) {
        Map<String,Object> targetCopy = new HashMap<String,Object>();
        if("C".equals(strategy)) {
            targetCopy.putAll(source);
            return targetCopy;
        }
        targetCopy.putAll(target);
        return handleFieldsForMerge(targetCopy, source, strategy);
    }
}
