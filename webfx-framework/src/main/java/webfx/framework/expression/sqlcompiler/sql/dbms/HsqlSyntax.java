package webfx.framework.expression.sqlcompiler.sql.dbms;

/**
 * @author Bruno Salmon
 */
public class HsqlSyntax extends DbmsSqlSyntaxImpl {

    private final static HsqlSyntax INSTANCE = new HsqlSyntax();

    public static HsqlSyntax get() {
        return INSTANCE;
    }

    public HsqlSyntax() {
        super(false, false);
    }
}
