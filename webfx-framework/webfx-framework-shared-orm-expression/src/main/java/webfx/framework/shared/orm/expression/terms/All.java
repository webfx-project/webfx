package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DataReader;

/**
 * @author Bruno Salmon
 */
public final class All<T> extends BinaryBooleanExpression<T> {

    public All(Expression<T> left, String operator, Expression<T> right) {
        super(left, operator + " all ", right, 5);
    }

    public Boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        throw new UnsupportedOperationException();
    }
}
