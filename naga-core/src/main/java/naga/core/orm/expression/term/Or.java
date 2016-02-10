package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class Or<T> extends BooleanExpression<T> {

    public Or(Expression<T> left, Expression<T> right) {
        super(left, " or ", right, 2);
    }

    public boolean isShortcutValue(Object value) {
        if (Boolean.TRUE.equals(value))
            return true;
        if (value instanceof Number)
            return ((Number) value).longValue() != 0;
        return value != null && !Boolean.FALSE.equals(value);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        return isShortcutValue(a) || isShortcutValue(b);
    }
}
