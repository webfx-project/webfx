package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;

/**
 * @author Bruno Salmon
 */
public class NotLike<T> extends BinaryBooleanExpression<T> {

    public NotLike(Expression<T> left, Expression<T> right) {
        super(left, " not like ", right, 5);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        return b instanceof String && !new Like.LikeImpl((String) b).compare(a);
    }

}
