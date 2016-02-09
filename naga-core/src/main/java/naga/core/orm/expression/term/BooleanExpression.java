package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.type.PrimType;
import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public abstract class BooleanExpression extends BinaryExpression {

    protected BooleanExpression(Expression left, String separator, Expression right, int precedenceLevel) {
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
