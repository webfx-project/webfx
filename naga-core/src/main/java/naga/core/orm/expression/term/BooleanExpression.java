package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.type.PrimType;
import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public abstract class BooleanExpression<T> extends BinaryExpression<T> {

    protected BooleanExpression(Expression<T> left, String separator, Expression<T> right, int precedenceLevel) {
        super(left, separator, right, precedenceLevel);
    }

    @Override
    public Type getType() {
        return PrimType.BOOLEAN;
    }

    @Override
    public Object evaluate(Object leftValue, Object rightValue) {
        return evaluateCondition(leftValue, rightValue);
    }

    public abstract boolean evaluateCondition(Object a, Object b);

}
