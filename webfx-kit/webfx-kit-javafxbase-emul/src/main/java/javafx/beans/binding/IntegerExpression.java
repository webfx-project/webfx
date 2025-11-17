package javafx.beans.binding;

import javafx.beans.value.ObservableIntegerValue;

/**
 * @author Bruno Salmon
 */
public abstract class IntegerExpression extends NumberExpressionBase implements
        ObservableIntegerValue {

    @Override
    public int intValue() {
        return get();
    }

    @Override
    public long longValue() {
        return (long) get();
    }

    @Override
    public float floatValue() {
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return (double) get();
    }

    @Override
    public Integer getValue() {
        return get();
    }

    @Override
    public DoubleBinding subtract(double other) {
        return Bindings.subtract(this, other);
    }

    @Override
    public LongBinding subtract(long other) {
        return (LongBinding) Bindings.subtract(this, other);
    }

    @Override
    public IntegerBinding subtract(int other) {
        return (IntegerBinding) Bindings.subtract(this, other);
    }

    @Override
    public DoubleBinding multiply(double other) {
        return Bindings.multiply(this, other);
    }

    @Override
    public LongBinding multiply(long other) {
        return (LongBinding) Bindings.multiply(this, other);
    }

    @Override
    public IntegerBinding multiply(int other) {
        return (IntegerBinding) Bindings.multiply(this, other);
    }
}
