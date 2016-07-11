package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class In<T> extends BinaryBooleanExpression<T> {

    public In(Expression<T> left, Expression<T> right) {
        super(left, " in ", right, 5);
    }

    @Override
    public Boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        return b instanceof List && ((List) b).contains(a);
    }
}
