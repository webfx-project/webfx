package webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms;

/**
 * @author Bruno Salmon
 */
public final class MySqlSyntax extends DbmsSqlSyntaxImpl {

    private final static MySqlSyntax INSTANCE = new MySqlSyntax();

    public static MySqlSyntax get() {
        return INSTANCE;
    }

    public MySqlSyntax() {
        super(true, false);
    }
}
