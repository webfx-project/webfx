package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;

/**
 * @author Bruno Salmon
 */
public class Any<T> extends BinaryBooleanExpression<T> {

    public Any(Expression<T> left, String operator, Expression<T> right) {
        super(left, operator + " any ", right, 5);
    }

    public Boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        throw new UnsupportedOperationException();
    }

}
