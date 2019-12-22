package webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms;

/**
 * @author Bruno Salmon
 */
public final class HsqlSyntax extends DbmsSqlSyntaxImpl {

    private final static HsqlSyntax INSTANCE = new HsqlSyntax();

    public static HsqlSyntax get() {
        return INSTANCE;
    }

    public HsqlSyntax() {
        super(false, false);
    }
}
