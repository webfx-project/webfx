package webfx.framework.client.orm.entity.filter;

import webfx.platform.shared.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class DqlClause {

    private final String dql;
    private final Object[] parameterValues;

    public static DqlClause create(CharSequence dql, Object... parameterValues) {
        return dql == null ? null : new DqlClause(dql, parameterValues);
    }

    public DqlClause(CharSequence dql, Object... parameterValues) {
        this.dql = dql.toString();
        this.parameterValues = parameterValues;
    }

    public static boolean isClauseTrue(DqlClause clause) {
        return isClauseEquals(clause, "true");
    }

    public static boolean isClauseFalse(DqlClause clause) {
        return isClauseEquals(clause, "false");
    }

    public static boolean isClause0(DqlClause clause) {
        return isClauseEquals(clause, "0");
    }

    private static boolean isClauseEquals(DqlClause clause, String value) {
        return clause != null && value.equalsIgnoreCase(clause.getDql());
    }

    public String getDql() {
        return dql;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    static Object[] concatArrays(Object[] array1, Object[] array2) {
        if (array1 == null || array1.length == 0)
            return array2;
        if (array2 == null || array2.length == 0)
            return array1;
        return Arrays.concat(Object[]::new, array1, array2);
    }

}
