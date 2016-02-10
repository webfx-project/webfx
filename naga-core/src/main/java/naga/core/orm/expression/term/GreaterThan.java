package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class GreaterThan<T> extends BooleanExpression<T> {

    public GreaterThan(Expression<T> left, Expression<T> right) {
        super(left, ">", right, 5);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        return PrimType.isGreaterThan((Number) a, (Number) b);
    }
}
