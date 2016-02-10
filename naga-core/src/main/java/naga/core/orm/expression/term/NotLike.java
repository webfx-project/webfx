package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class NotLike<T> extends BooleanExpression<T> {

    public NotLike(Expression<T> left, Expression<T> right) {
        super(left, " not like ", right, 5);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        if (b instanceof String)
            return !new Like.LikeImpl((String) b).compare(a);
        return false;
    }

}
