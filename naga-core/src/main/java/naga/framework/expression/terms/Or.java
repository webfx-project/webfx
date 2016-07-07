package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.commons.util.Booleans;
import naga.commons.util.Numbers;

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
    public boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        return isShortcutValue(a) || isShortcutValue(b);
    }
}
