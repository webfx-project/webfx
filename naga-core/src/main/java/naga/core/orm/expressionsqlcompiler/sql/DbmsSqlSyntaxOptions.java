package naga.core.orm.expressionsqlcompiler.sql;

/**
 * @author Bruno Salmon
 */
public class DbmsSqlSyntaxOptions {

    private final boolean repeatDeleteAlias;

    public DbmsSqlSyntaxOptions(boolean repeatDeleteAlias) {
        this.repeatDeleteAlias = repeatDeleteAlias;
    }

    public boolean isRepeatDeleteAlias() {
        return repeatDeleteAlias;
    }

    public final static DbmsSqlSyntaxOptions POSTGRES_SYNTAX = new DbmsSqlSyntaxOptions(true);
    public final static DbmsSqlSyntaxOptions MYSQL_SYNTAX = new DbmsSqlSyntaxOptions(true);
    public final static DbmsSqlSyntaxOptions HSQL_SYNTAX = new DbmsSqlSyntaxOptions(false);
}
