package webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms;

/**
 * @author Bruno Salmon
 */
public interface DbmsSqlSyntax {

    boolean repeatTableAliasAfterDelete();

    boolean hasInsertReturningClause();

    default String quoteTableIfReserved(String sqlTableName) {
        return quoteIdentifierIfReserved(sqlTableName);
    }

    default String quoteColumnIfReserved(String sqlColumnName) {
        return quoteIdentifierIfReserved(sqlColumnName);
    }

    /*private*/ default String quoteIdentifierIfReserved(String identifier) {
        return isReservedIdentifier(identifier) ? quoteIdentifier(identifier) : identifier;
    }

    default String quoteIdentifier(String identifier) {
        return "\"" + identifier + "\"";
    }

    default boolean isReservedIdentifier(String identifier) {
        return false;
    }

}
