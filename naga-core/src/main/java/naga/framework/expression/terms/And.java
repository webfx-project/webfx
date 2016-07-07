package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.lci.DataWriter;
import naga.commons.util.Numbers;

/**
 * @author Bruno Salmon
 */
public class And<T> extends BinaryBooleanExpression<T> {

    public And(Expression<T> left, Expression<T> right) {
        super(left, " and ", right, 3);
    }

    public boolean isShortcutValue(Object value) {
        return value == null || value.equals(Boolean.FALSE) || Numbers.isZero(value);
    }

    @Override
    public void setValue(T domainObject, Object value, DataWriter<T> dataWriter) {
        left.setValue(domainObject, value, dataWriter);
        right.setValue(domainObject, value, dataWriter);
    }

    @Override
    public boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader) {
        return !isShortcutValue(a) && !isShortcutValue(b);
    }

}
