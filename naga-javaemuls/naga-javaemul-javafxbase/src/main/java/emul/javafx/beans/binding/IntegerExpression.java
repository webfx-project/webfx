package emul.javafx.beans.binding;

import emul.javafx.beans.value.ObservableIntegerValue;

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
}
