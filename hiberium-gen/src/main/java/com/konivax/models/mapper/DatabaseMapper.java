package com.konivax.models.mapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * config support for commonly used relational databases
 */
public final class DatabaseMapper {

    //template properties
    private static String PROP_DRIVER = "dbsource_driver";
    private static String PROP_LIBRARY = "dbsource_library";
    private static String PROP_DIALECT = "hibernate_dialect";
    private static String PROP_STARTUP = "hibernate_startup";

    private static List<String> sqlKeywords = Arrays.asList(
            "database", "table", "column", "view", "procedure", "trigger",
            "exists", "create", "alter", "add", "drop", "delete", "replace", "truncate", "set", "backup",
            "constraint", "key", "primary", "unique", "foreign", "index", "check", "references", "default",
            "select", "insert", "update", "delete", "values", "from", "into", "exec",
            "and", "or", "not", "in", "as", "by", "is", "like", "any", "all", "null",
            "order", "asc", "desc", "limit", "offset", "top",
            "join", "inner", "outer", "left", "right", "full", "union", "on", "using",
            "where", "rownum", "group", "having", "distinct", "between", "case");

    private DatabaseMapper() { }

    public static Boolean validateSqlFieldName(String fieldName) {
        return !sqlKeywords.contains(fieldName.toLowerCase());
    }

    public static Map<String,String> mapDatabaseToDriver(String dbName) {
        String sourceDriver = "";
        String sourceLibrary = ""; //gradle dependency runtimeOnly
        String hibernateDialect = "";
        String hibernateStartup = "validate"; //none ; validate ; update ; create ; create-drop

        switch (dbName) {
            case "sybase":
                sourceDriver = "com.sybase.jdbc3.jdbc.SybDriver";
                sourceLibrary = "com.esen.jdbc:sybase-jconn3"+":6.0";
                hibernateDialect = "org.hibernate.dialect.SybaseDialect";
                break;
            case "postgres":
                sourceDriver = "org.postgresql.Driver";
                sourceLibrary = "org.postgresql:postgresql";
                hibernateDialect = "org.hibernate.dialect.PostgreSQLDialect";
                break;
            case "mysql":
                sourceDriver = "com.mysql.jdbc.Driver";
                sourceLibrary = "mysql:mysql-connector-java"+":5.1.6";
                hibernateDialect = "org.hibernate.dialect.MySQLDialect";
                break;
            case "oracle":
                sourceDriver = "oracle.jdbc.OracleDriver";
                sourceLibrary = "com.oracle.ojdbc:ojdbc8"+":19.3.0.0";
                hibernateDialect = "org.hibernate.dialect.OracleDialect";
                break;
            case "db2":
                sourceDriver = "com.ibm.db2.jcc.DB2Driver";
                sourceLibrary = "com.ibm.db2.jcc:db2jcc:db2jcc4";
                hibernateDialect = "org.hibernate.dialect.DB2Dialect";
                break;
            case "sqlserver":
                sourceDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                sourceLibrary = "com.microsoft.sqlserver:mssql-jdbc"+":8.4.1.jre8";
                hibernateDialect = "org.hibernate.dialect.SQLServerDialect";
                break;
            case "h2":
            default:
                sourceDriver = "org.h2.Driver";
                sourceLibrary = "com.h2database:h2";
                hibernateDialect = "org.hibernate.dialect.H2Dialect";
                hibernateStartup = "create-drop";
                break;
        }

        Map<String,String> databaseVars = new HashMap<String,String>();
        databaseVars.put(PROP_DRIVER, sourceDriver);
        databaseVars.put(PROP_LIBRARY, sourceLibrary);
        databaseVars.put(PROP_DIALECT, hibernateDialect);
        databaseVars.put(PROP_STARTUP, hibernateStartup);
        return databaseVars;
    }

}
