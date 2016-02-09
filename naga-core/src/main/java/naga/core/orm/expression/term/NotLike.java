package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class NotLike extends BooleanExpression {
    @Override
    public boolean evaluateCondition(Object a, Object b) {
        if (b instanceof String)
            return !new Like.LikeImpl((String) b).compare(a);
        return false;
    }

    public NotLike(Expression left, Expression right) {
        super(left, " not like ", right, 5);
    }

}
