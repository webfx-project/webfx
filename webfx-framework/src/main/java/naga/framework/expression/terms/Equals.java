package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataWriter;
import naga.util.Booleans;
import naga.util.Objects;

/**
 * @author Bruno Salmon
 */
public class Equals<T> extends PrimitiveBinaryBooleanExpression<T> {

    public Equals(Expression<T> left, Expression<T> right) {
        super(left, "=", right, 5);
    }

    @Override
    boolean evaluateInteger(int a, int b) {
        return a == b;
    }

    @Override
    boolean evaluateLong(long a, long b) {
        return a == b;
    }

    @Override
    boolean evaluateFloat(float a, float b) {
        return a == b;
    }

    @Override
    boolean evaluateDouble(double a, double b) {
        return a == b;
    }

    @Override
    boolean evaluateBoolean(boolean a, boolean b) {
        return a == b;
    }

    @Override
    boolean evaluateObject(Object a, Object b) {
        return Objects.areEquals(a, b);
    }

    @Override
    public void setValue(T domainObject, Object value, DataWriter<T> dataWriter) {
        if (Booleans.isTrue(value))
            left.setValue(domainObject, right.evaluate(domainObject, dataWriter), dataWriter);
    }

}
