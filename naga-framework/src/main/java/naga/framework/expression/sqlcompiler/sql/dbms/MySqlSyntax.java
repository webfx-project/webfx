package naga.framework.expression.sqlcompiler.sql.dbms;

/**
 * @author Bruno Salmon
 */
public class MySqlSyntax extends DbmsSqlSyntaxImpl {

    private final static MySqlSyntax INSTANCE = new MySqlSyntax();

    public static MySqlSyntax get() {
        return INSTANCE;
    }

    public MySqlSyntax() {
        super(true, false);
    }
}
