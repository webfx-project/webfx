package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class GreaterThanOrEquals<T> extends BooleanExpression<T> {

    public GreaterThanOrEquals(Expression<T> left, Expression<T> right) {
        super(left, ">=", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b) {
        Number n1 = (Number) a, n2 = (Number) b;
        return PrimType.isGreaterThan(n1, n2) || PrimType.areNumberEquivalent(n1, n2);
    }

}
