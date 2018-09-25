package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.lci.DataReader;
import webfx.platforms.core.util.Arrays;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class In<T> extends BinaryBooleanExpression<T> {

    public In(Expression<T> left, Expression<T> right) {
        super(left, " in ", right, 5);
    }

    @Override
    public Boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        if (b instanceof Object[])
            return Arrays.contains((Object[]) b, a);
        if (b instanceof Collection)
            return ((Collection) b).contains(a);
        return false;
    }
}
