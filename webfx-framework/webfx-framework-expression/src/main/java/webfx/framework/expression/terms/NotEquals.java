package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;
import webfx.platforms.core.util.Objects;

/**
 * @author Bruno Salmon
 */
public class NotEquals<T> extends PrimitiveBinaryBooleanExpression<T> {

    public NotEquals(Expression<T> left, Expression<T> right) {
        super(left, "!=", right, 5);
    }

    @Override
    boolean evaluateInteger(int a, int b) {
        return a != b;
    }

    @Override
    boolean evaluateLong(long a, long b) {
        return a != b;
    }

    @Override
    boolean evaluateFloat(float a, float b) {
        return a != b;
    }

    @Override
    boolean evaluateDouble(double a, double b) {
        return a != b;
    }

    @Override
    boolean evaluateBoolean(boolean a, boolean b) {
        return a != b;
    }

    @Override
    boolean evaluateObject(Object a, Object b) {
        return !Objects.areEquals(a, b);
    }
}
