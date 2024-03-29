package com.konivax.models.mapper;

/**
 * fields that have standard defined behaviour
 */
public final class FieldConstants {

    private FieldConstants() { }

    //supported types
    public static final String TYPE_STRING = "String";
    public static final String TYPE_INTEGER = "Integer";
    public static final String TYPE_LONG = "Long";
    public static final String TYPE_DOUBLE = "Double";
    public static final String TYPE_DATETIME = "Date";
    public static final String TYPE_BOOLEAN = "Boolean";
    public static final String TYPE_CHARACTER = "Character";
    public static final String TYPE_BINARY = "byte[]";

    public static final String TYPE_PRIMARY = TYPE_INTEGER;

    //attribute roles
    public static final String ROLE_PRIMARY = "K";
    public static final String ROLE_HIDDEN = "H";
    public static final String ROLE_UNIQUE = "U";
    public static final String ROLE_FOREIGN = "F";
    public static final String ROLE_NOTNULL = "N";
    public static final String ROLE_FINAL = "I"; //immutable
    public static final String ROLE_SEARCH = "R";
    public static final String ROLE_DEFAULT = "S"; //standard

    //todo attribute behaviors
    public static final String CODE_PRIMARY = "primaryKey";
    public static final String CODE_ACTIVE = "activeInd";
    public static final String CODE_CREATED = "createTs";
    public static final String CODE_UPDATED = "updateTs";
    public static final String CODE_STATUS = "statusCd";
    public static final String CODE_WORKFLOW = "workflowSt";
    public static final String CODE_VERSION = "version";

    public static final String KEY_PRIMARY = "pk";
    public static final String KEY_UNIQUE = "uk";
    public static final String KEY_FOREIGN = "fk";

    //data constants
    public static final Integer DEFAULT_LENGTH = 128;
    public static final Integer DEFAULT_SCALE = 4;
    public static final Integer DEFAULT_PRECISION = 22;
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String DATA_ACTIVE = "1";
    public static final String DATA_INACTIVE = "0";

    public static final String DATA_INSERT = "+1";
    public static final String DATA_UPDATE = "+0";
    public static final String DATA_DELETE = "-1";

    public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss.SSSSS";
    public static final String DATA_DATETS = "new Date()";

}
