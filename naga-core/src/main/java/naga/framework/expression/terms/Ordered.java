package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.commons.util.Booleans;

/**
 * @author Bruno Salmon
 */
public class Ordered<T> extends UnaryExpression<T> {
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
    public Object evaluate(T domainObject, DataReader<T> dataReader) {
        return Booleans.isFalse(super.evaluate(domainObject, dataReader));
    }

    @Override
    public Type getType() {
        return PrimType.BOOLEAN;
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
