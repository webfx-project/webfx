package naga.core.orm.expression.term;

/**
 * @author Bruno Salmon
 */
public class And extends BooleanExpression {

    public And(Expression left, Expression right) {
        super(left, " and ", right, 3);
    }

    public boolean isShortcutValue(Object value) {
        if (Boolean.FALSE.equals(value))
            return true;
        if (value instanceof Number)
            return ((Number) value).longValue() == 0;
        return value == null;
    }

    @Override
    public void setValue(Object data, Object value) {
        left.setValue(data, value);
        right.setValue(data, value);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        return !isShortcutValue(a) && !isShortcutValue(b);
    }

}
