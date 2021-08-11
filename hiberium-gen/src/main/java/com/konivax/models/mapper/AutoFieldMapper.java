package com.konivax.models.mapper;

/**
 * fields that have standard defined behaviour
 */
public final class AutoFieldMapper {

    private AutoFieldMapper() { }

    //attribute flags
    public static final String FLAG_PRIMARY = "K";
    public static final String FLAG_HIDDEN = "H";
    public static final String FLAG_UNIQUE = "U";
    public static final String FLAG_FINAL = "F";
    public static final String FLAG_FOREIGN = "M";
    public static final String FLAG_SEARCH = "R";
    public static final String FLAG_NOTNULL = "N";
    public static final String FLAG_DEFAULT = "S";

    //todo attribute behaviors
    public static final String ROLE_PRIMARY = "primary";
    public static final String ROLE_ACTIVE = "active";
    public static final String ROLE_CREATETS = "createTs";
    public static final String ROLE_UPDATETS = "updateTs";
    public static final String ROLE_STATUS = "statusCd";
    public static final String ROLE_WORKFLOW = "workflow";

    //todo update strategy

    //data constants
    public static final String DATA_ACTIVE = "1";
    public static final String DATA_INACTIVE = "0";

    public static final String DATA_DATETS = "new Date()";

    //cardinality
    public static final String ONE_TO_ONE = "1:1";
    public static final String ONE_TO_MANY = "1:n";

}
