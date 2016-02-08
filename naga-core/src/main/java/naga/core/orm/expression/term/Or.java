package naga.core.orm.expression.term;

/**
 * @author Bruno Salmon
 */
public class Or extends BooleanExpression {

    public Or(Expression left, Expression right) {
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
