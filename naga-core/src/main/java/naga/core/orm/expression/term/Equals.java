package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.lci.DataWriter;
import naga.core.util.Booleans;
import naga.core.util.Objects;

/**
 * @author Bruno Salmon
 */
public class Equals<T> extends BinaryBooleanExpression<T> {

    public Equals(Expression<T> left, Expression<T> right) {
        super(left, "=", right, 5);
    }

    public boolean evaluateCondition(Object a, Object b) {
        return Objects.areEquivalent(a, b);
    }

    @Override
    public void setValue(T domainObject, Object value, DataWriter<T> dataWriter) {
        if (Booleans.isTrue(value))
            left.setValue(domainObject, right.evaluate(domainObject, dataWriter), dataWriter);
    }

}
