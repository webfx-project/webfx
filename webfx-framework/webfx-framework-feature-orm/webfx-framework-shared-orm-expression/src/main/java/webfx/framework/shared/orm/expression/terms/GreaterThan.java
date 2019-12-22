package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public final class GreaterThan<T> extends PrimitiveBinaryBooleanExpression<T> {

    public GreaterThan(Expression<T> left, Expression<T> right) {
        super(left, ">", right, 5);
    }

    @Override
    boolean evaluateInteger(int a, int b) {
        return a > b;
    }

    @Override
    boolean evaluateLong(long a, long b) {
        return a > b;
    }

    @Override
    boolean evaluateFloat(float a, float b) {
        return a > b;
    }

    @Override
    boolean evaluateDouble(double a, double b) {
        return a > b;
    }

    @Override
    boolean evaluateBoolean(boolean a, boolean b) {
        return a && !b;
    }

    @Override
    boolean evaluateObject(Object a, Object b) {
        return ((Comparable) a).compareTo(b) > 0;
    }
}
