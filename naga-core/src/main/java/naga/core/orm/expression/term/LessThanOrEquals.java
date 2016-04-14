package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class LessThanOrEquals<T> extends PrimitiveBinaryBooleanExpression<T> {

    public LessThanOrEquals(Expression<T> left, Expression<T> right) {
        super(left, "<=", right, 5);
    }

    @Override
    boolean evaluateInteger(int a, int b) {
        return a <= b;
    }

    @Override
    boolean evaluateLong(long a, long b) {
        return a <= b;
    }

    @Override
    boolean evaluateFloat(float a, float b) {
        return a <= b;
    }

    @Override
    boolean evaluateDouble(double a, double b) {
        return a <= b;
    }

    @Override
    boolean evaluateBoolean(boolean a, boolean b) {
        return !a && b || a == b;
    }

    @Override
    boolean evaluateObject(Object a, Object b) {
        return ((Comparable) a).compareTo(b) <= 0;
    }

}
