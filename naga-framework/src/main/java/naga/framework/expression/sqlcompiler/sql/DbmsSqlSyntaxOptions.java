package naga.framework.expression.sqlcompiler.sql;

/**
 * @author Bruno Salmon
 */
public class DbmsSqlSyntaxOptions {

    private final boolean repeatDeleteAlias;
    private final boolean insertReturningClause;

    public DbmsSqlSyntaxOptions(boolean repeatDeleteAlias, boolean insertReturningClause) {
        this.repeatDeleteAlias = repeatDeleteAlias;
        this.insertReturningClause = insertReturningClause;
    }

    public boolean isRepeatDeleteAlias() {
        return repeatDeleteAlias;
    }

    public boolean hasInsertReturningClause() {
        return insertReturningClause;
    }

    public final static DbmsSqlSyntaxOptions POSTGRES_SYNTAX = new DbmsSqlSyntaxOptions(false, true);
    public final static DbmsSqlSyntaxOptions MYSQL_SYNTAX = new DbmsSqlSyntaxOptions(true, false);
    public final static DbmsSqlSyntaxOptions HSQL_SYNTAX = new DbmsSqlSyntaxOptions(false, false);
}
