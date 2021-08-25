package com.konivax.models.merge;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * same as template merge-object-java.ftl
 * @author therohk 2021/08/16
 */
public final class MergeObject {

    //update strategy
    public static final String PROC_C = "C"; //always create new instance
    public static final String PROC_N = "N"; //ignore request ; reject all
    public static final String PROC_Y = "Y"; //insert, update or delete ; accept all
    public static final String PROC_B = "B"; //insert or update, no delete
    public static final String PROC_H = "H"; //update value only if exists
    public static final String PROC_I = "I"; //insert only, no update or delete
    public static final String PROC_U = "U"; //update or delete only, no insert
    public static final String PROC_D = "D"; //delete only, no update or insert

    public static final String PROC_M = "M"; //vector merge for delimited text

    //default strategy
    public static final String PROC_INSERT = PROC_C;
    public static final String PROC_DELETE = PROC_B;
    public static final String PROC_UPDATE = PROC_B;

    private MergeObject() { }

    /**
     * field values are copied from source to target
     * @param target the existing db tuple
     * @param source the request to modify it
     * @param fieldName field under consideration for merge
     * @param strategy merge logic will depend on this flag
     */
    public static <T> void handleFieldForMerge(T target, final T source, String fieldName, String strategy) {
        assert target != null : "target object is null";
        assert source != null : "source object is null";
        assert fieldName != null && !fieldName.isEmpty() : "field name not defined";
        assert strategy.length() == 1 : "strategy code is invalid";
//        Field field = ReflectUtils.getEntityFieldByName(target.getClass(), fieldName);
//        Validate.isTrue(field != null, "field name does not exist");

        Object targetVal = invokeGetter(target, fieldName);
        Object sourceVal = invokeGetter(source, fieldName);
        boolean hasTargetVal = targetVal != null;
        boolean hasSourceVal = sourceVal != null;
        if(!hasTargetVal && !hasSourceVal)
            return;
        if(hasTargetVal && targetVal.equals(sourceVal))
            return;

        if(approveTargetSetter(strategy, hasTargetVal, hasSourceVal))
            invokeSetter(target, fieldName, sourceVal);
    }

    /**
     * decide if value can be transitioned from source to target
     * decision is based on flag and conditions
     * @param strategy chosen update strategy
     * @param hasTargetVal is target != null
     * @param hasSourceVal is source != null
     * @return setting target value is allowed or not
     */
    public static boolean approveTargetSetter(final String strategy, boolean hasTargetVal, boolean hasSourceVal) {
        boolean approveSetter;
        switch (strategy) {
            case PROC_C:
            case PROC_N:
                approveSetter = false;
                break;
            case PROC_Y: //aya
                approveSetter = true;
                break;
            case PROC_B:
                approveSetter = hasSourceVal;
                break;
            case PROC_U:
                approveSetter = hasTargetVal;
                break;
            case PROC_D:
                approveSetter = !hasSourceVal;
                break;
            case PROC_H:
                approveSetter = hasTargetVal && hasSourceVal;
                break;
            case PROC_I:
                approveSetter = !hasTargetVal;
                break;
            default:
                throw new IllegalStateException("strategy code not supported");
        }
        return approveSetter;
    }

    public static String pickStrategyCode(String opCode) {
        switch (opCode.toLowerCase()) {
            case "create":
            case "insert":
            case "post":
                return PROC_INSERT;
            case "update":
            case "modify":
            case "put":
                return PROC_UPDATE;
            case "remove":
            case "delete":
                return PROC_DELETE;
            case "select":
            case "ignore":
            case "get":
            default:
                return PROC_N;
        }
    }

    public static Object invokeGetter(Object obj, String fieldName) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
            Method getter = pd.getReadMethod();
            try {
                return getter.invoke(obj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException iae) {
                throw new RuntimeException(iae);
            }
        } catch (IntrospectionException ie) {
            throw new RuntimeException(ie);
        }
    }

    public static void invokeSetter(Object obj, String fieldName, Object fieldValue) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
            Method setter = pd.getWriteMethod();
            try {
                setter.invoke(obj, fieldValue);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException iae) {
                throw new RuntimeException(iae);
            }
        } catch (IntrospectionException ie) {
            throw new RuntimeException(ie);
        }
    }
}
