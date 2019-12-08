package webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms;

/**
 * @author Bruno Salmon
 */
public final class PostgresSyntax extends DbmsSqlSyntaxImpl {

    private final static PostgresSyntax INSTANCE = new PostgresSyntax();

    public static PostgresSyntax get() {
        return INSTANCE;
    }

    public PostgresSyntax() {
        super(false, true);
    }

    public boolean isReservedIdentifier(String identifier) {
        switch (identifier = identifier.toLowerCase()) {
            case "analyse":
            case "analyze":
            case "concurrently":
            case "do":
            case "freeze":
            case "isnull":
            case "limit":
            case "notnull":
            case "placing":
            case "returning":
            case "variadic":
            case "verbose":
                return true;
        }
        return super.isReservedIdentifier(identifier);
    }
}
