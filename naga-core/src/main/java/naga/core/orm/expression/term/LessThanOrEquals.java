package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class LessThanOrEquals<T> extends BooleanExpression<T> {

    public LessThanOrEquals(Expression<T> left, Expression<T> right) {
        super(left, "<=", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b) {
        Number n1 = (Number) a, n2 = (Number) b;
        return PrimType.isLessThan(n1, n2) || PrimType.areNumberEquivalent(n1, n2);
    }

}
