package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.util.Booleans;
import naga.core.util.Numbers;

/**
 * @author Bruno Salmon
 */
public class Or<T> extends BinaryBooleanExpression<T> {

    public Or(Expression<T> left, Expression<T> right) {
        super(left, " or ", right, 2);
    }

    public boolean isShortcutValue(Object value) {
        return value != null && Booleans.isNotFalse(value) && Numbers.isNotZero(value);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        return isShortcutValue(a) || isShortcutValue(b);
    }
}
