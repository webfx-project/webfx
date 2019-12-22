package webfx.framework.shared.orm.dql;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class DqlClause {

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
        return isClauseEquals(clause, true);
    }

    public static boolean isClauseFalse(DqlClause clause) {
        return isClauseEquals(clause, false);
    }

    public static boolean isClause0(DqlClause clause) {
        return isClauseEquals(clause, 0);
    }

    private static boolean isClauseEquals(DqlClause clause, Object value) {
        if (clause == null)
            return false;
        String dql = clause.getDql();
        if (dql == null)
            return false;
        if (!dql.equals("?"))
            return dql.equalsIgnoreCase(String.valueOf(value));
        Object[] parameterValues = clause.parameterValues;
        if (parameterValues == null || parameterValues.length != 1)
            return false;
        return Objects.equals(parameterValues[0], value);
    }

    public String getDql() {
        return dql;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    static Object[] concatClauseParameterValues(DqlClause... clauses) {
        return Arrays.stream(clauses)
                .filter(clause -> clause != null && clause.dql != null && clause.parameterValues != null)
                .flatMap(clause -> Arrays.stream(clause.parameterValues))
                .toArray();
    }
}
