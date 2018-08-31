package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.lci.DataReader;

/**
 * @author Bruno Salmon
 */
public class NotLike<T> extends BinaryBooleanExpression<T> {

    public NotLike(Expression<T> left, Expression<T> right) {
        super(left, " not like ", right, 5);
    }

    @Override
    public Boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        return b instanceof String && !new Like.LikeImpl((String) b).compare(a);
    }

}
