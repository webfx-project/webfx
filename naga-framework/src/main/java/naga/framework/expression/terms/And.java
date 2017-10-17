package naga.framework.expression.terms;

import naga.util.Booleans;
import naga.util.Numbers;
import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.lci.DataWriter;

/**
 * @author Bruno Salmon
 */
public class And<T> extends BinaryBooleanExpression<T> {

    public And(Expression<T> left, Expression<T> right) {
        super(left, " and ", right, 3);
    }

    /**
     * A shortcut value for the And operator is a value that will make the operator return false whatever the other operand is.
     * A strict And operator would accept only false as shortcut value but since this operator is not strict, it accepts
     * false and 0 as a shortcut values.
     */
    public boolean isShortcutValue(Object value) {
        return value == null || Booleans.isFalse(value) || Numbers.isZero(value);
    }

    @Override
    public Boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        if (isShortcutValue(a) || isShortcutValue(b))
            return false;
        if (a == null || b == null)
            return null;
        return true;
    }

    @Override
    public void setValue(T domainObject, Object value, DataWriter<T> dataWriter) {
        left.setValue(domainObject, value, dataWriter);
        right.setValue(domainObject, value, dataWriter);
    }

}
