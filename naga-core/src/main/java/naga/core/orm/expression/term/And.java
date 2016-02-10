package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataWriter;

/**
 * @author Bruno Salmon
 */
public class And<T> extends BooleanExpression<T> {

    public And(Expression<T> left, Expression<T> right) {
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
    public void setValue(T domainObject, Object value, DataWriter<T> dataWriter) {
        left.setValue(domainObject, value, dataWriter);
        right.setValue(domainObject, value, dataWriter);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b) {
        return !isShortcutValue(a) && !isShortcutValue(b);
    }

}
