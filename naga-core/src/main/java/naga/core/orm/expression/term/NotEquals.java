package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.util.Objects;

/**
 * @author Bruno Salmon
 */
public class NotEquals<T> extends BinaryBooleanExpression<T> {

    public NotEquals(Expression<T> left, Expression<T> right) {
        super(left, "!=", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b) {
        return !Objects.areEquivalent(a, b);
    }

}
