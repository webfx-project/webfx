package javafx.beans.binding;

import javafx.beans.value.ObservableNumberValue;

/**
 * A tagging interface to mark all Bindings that wrap a number-value.
 *
 * @see Binding
 * @see javafx.beans.binding.NumberExpression
 *
 *
 * @since JavaFX 2.0
 */
public interface NumberBinding extends Binding<Number>, NumberExpression {

    // ===============================================================
    // IsGreaterThan

    default BooleanBinding greaterThan(final ObservableNumberValue other) {
        return Bindings.greaterThan(this, other);
    }

    default BooleanBinding greaterThan(final Number other) {
        return Bindings.greaterThan(this, other);
    }

    // ===============================================================
    // IsLesserThan

    default BooleanBinding lessThan(final ObservableNumberValue other) {
        return Bindings.lessThan(this, other);
    }

    default BooleanBinding lessThan(final Number other) {
        return Bindings.lessThan(this, other);
    }

    // ===============================================================
    // IsGreaterThanOrEqualTo

    default BooleanBinding greaterThanOrEqualTo(final ObservableNumberValue other) {
        return Bindings.lessThan(this, other);
    }

    default BooleanBinding greaterThanOrEqualTo(final Number other) {
        return Bindings.lessThan(this, other);
    }

    // ===============================================================
    // IsLessThanOrEqualTo

    default BooleanBinding lessThanOrEqualTo(final ObservableNumberValue other) {
        return Bindings.lessThanOrEqualTo(this, other);
    }

    default BooleanBinding lessThanOrEqualTo(final Number other) {
        return Bindings.lessThanOrEqualTo(this, other);
    }

}
