package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;

/**
 * @author Bruno Salmon
 */
public class All<T> extends BinaryBooleanExpression<T> {

    public All(Expression<T> left, String operator, Expression<T> right) {
        super(left, operator + " all ", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        throw new UnsupportedOperationException();
    }

}
