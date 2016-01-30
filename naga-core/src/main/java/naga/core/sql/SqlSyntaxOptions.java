package naga.core.sql;

/**
 * @author Bruno Salmon
 */
public class SqlSyntaxOptions {

    private final boolean repeatDeleteAlias;

    public SqlSyntaxOptions(boolean repeatDeleteAlias) {
        this.repeatDeleteAlias = repeatDeleteAlias;
    }

    public boolean isRepeatDeleteAlias() {
        return repeatDeleteAlias;
    }
}
