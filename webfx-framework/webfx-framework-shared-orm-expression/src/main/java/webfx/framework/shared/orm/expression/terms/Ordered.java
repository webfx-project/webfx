package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public final class Ordered<T> extends UnaryExpression<T> {
    private final boolean ascending;
    private final boolean descending;
    private final boolean nullsFirst;
    private final boolean nullsLast;

    public Ordered(Expression<T> operand, boolean ascending, boolean descending, boolean nullsFirst, boolean nullsLast) {
        super(operand);
        this.ascending = ascending;
        this.descending = descending;
        this.nullsFirst = nullsFirst;
        this.nullsLast = nullsLast;
    }

    public boolean isAscending() {
        return ascending;
    }

    public boolean isDescending() {
        return descending;
    }

    public boolean isNullsFirst() {
        return nullsFirst;
    }

    public boolean isNullsLast() {
        return nullsLast;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        operand.toString(sb);
        if (ascending)
            sb.append(" asc");
        else if (descending)
            sb.append(" desc");
        if (nullsFirst)
            sb.append(" nulls first");
        else if (nullsLast)
            sb.append(" nulls last");
        return sb;
    }
}
