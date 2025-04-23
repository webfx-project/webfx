/*
 * Copyright (c) 2010, 2016, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package javafx.beans.binding;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.IntegerConstant;
import com.sun.javafx.collections.ImmutableObservableList;
import dev.webfx.platform.util.Strings;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Bindings is a helper class with a lot of utility functions to create simple
 * bindings.
 * <p>
 * Usually there are two possibilities to define the same operation: the Fluent
 * API and the the factory methods in this class. This allows a developer to
 * define complex expression in a way that is most easy to understand. For
 * instance the expression {@code result = a*b + c*d} can be defined using only
 * the Fluent API:
 * <p>
 * {@code DoubleBinding result = a.multiply(b).add(c.multiply(d));}
 * <p>
 * Or using only factory methods in Bindings:
 * <p>
 * {@code NumberBinding result = add (multiply(a, b), multiply(c,d));}
 * <p>
 * Or mixing both possibilities:
 * <p>
 * {@code NumberBinding result = add (a.multiply(b), c.multiply(d));}
 * <p>
 * The main difference between using the Fluent API and using the factory
 * methods in this class is that the Fluent API requires that at least one of
 * the operands is an Expression (see {@link javafx.beans.binding}). (Every
 * Expression contains a static method that generates an Expression from an
 * {@link javafx.beans.value.ObservableValue}.)
 * <p>
 * Also if you watched closely, you might have noticed that the return type of
 * the Fluent API is different in the examples above. In a lot of cases the
 * Fluent API allows to be more specific about the returned type (see
 * {@link javafx.beans.binding.NumberExpression} for more details about implicit
 * casting.
 *
 * @see Binding
 * @see NumberBinding
 *
 *
 * @since JavaFX 2.0
 */
public final class Bindings {

    private Bindings() {
    }
    // =================================================================================================================
    // Helper functions to create custom bindings

    /**
     * Helper function to create a custom {@link BooleanBinding}.
     *
     * @param func The function that calculates the value of this binding
     * @param dependencies The dependencies of this binding
     * @return The generated binding
     * @since JavaFX 2.1
     */
    public static BooleanBinding createBooleanBinding(final Callable<Boolean> func, final Observable... dependencies) {
        return new BooleanBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    //Logging.getLogger().warning("Exception while evaluating binding", e);
                    return false;
                }
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            //@ReturnsUnmodifiableCollection
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                        FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    /**
     * Creates a {@link javafx.beans.binding.BooleanBinding} that calculates the inverse of the value
     * of a {@link javafx.beans.value.ObservableBooleanValue}.
     *
     * @param op
     *            the {@code ObservableBooleanValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the operand is {@code null}
     */
    public static BooleanBinding not(final ObservableBooleanValue op) {
        return createBooleanBinding(() -> !op.get(), op);
    }

    // =================================================================================================================
    // Bidirectional Bindings

    /**
     * Generates a bidirectional binding (or "bind with inverse") between two
     * instances of {@link javafx.beans.property.Property}.
     * <p>
     * A bidirectional binding is a binding that works in both directions. If
     * two properties {@code a} and {@code b} are linked with a bidirectional
     * binding and the value of {@code a} changes, {@code b} is set to the same
     * value automatically. And vice versa, if {@code b} changes, {@code a} is
     * set to the same value.
     * <p>
     * A bidirectional binding can be removed with
     * {@link #unbindBidirectional(Property, Property)}.
     * <p>
     * Note: this implementation of a bidirectional binding behaves differently
     * from all other bindings here in two important aspects. A property that is
     * linked to another property with a bidirectional binding can still be set
     * (usually bindings would throw an exception). Secondly bidirectional
     * bindings are calculated eagerly, i.e. a bound property is updated
     * immediately.
     *
     * @param <T>
     *            the types of the properties
     * @param property1
     *            the first {@code Property<T>}
     * @param property2
     *            the second {@code Property<T>}
     * @throws NullPointerException
     *            if one of the properties is {@code null}
     * @throws IllegalArgumentException
     *            if both properties are equal
     */
    public static <T> void bindBidirectional(Property<T> property1, Property<T> property2) {
        BidirectionalBinding.bind(property1, property2);
    }

    /**
     * Delete a bidirectional binding that was previously defined with
     * {@link #bindBidirectional(Property, Property)}.
     *
     * @param <T>
     *            the types of the properties
     * @param property1
     *            the first {@code Property<T>}
     * @param property2
     *            the second {@code Property<T>}
     * @throws NullPointerException
     *            if one of the properties is {@code null}
     * @throws IllegalArgumentException
     *            if both properties are equal
     */
    public static <T> void unbindBidirectional(Property<T> property1, Property<T> property2) {
        BidirectionalBinding.unbind(property1, property2);
    }

    /**
     * Delete a bidirectional binding that was previously defined with
     * {@link #bindBidirectional(Property, Property)} or
     * {@link #bindBidirectional(javafx.beans.property.Property, javafx.beans.property.Property, java.text.Format)}.
     *
     * @param property1
     *            the first {@code Property<T>}
     * @param property2
     *            the second {@code Property<T>}
     * @throws NullPointerException
     *            if one of the properties is {@code null}
     * @throws IllegalArgumentException
     *            if both properties are equal
     * @since JavaFX 2.1
     */
    public static void unbindBidirectional(Object property1, Object property2) {
        BidirectionalBinding.unbind(property1, property2);
    }

    /**
     * Generates a bidirectional binding (or "bind with inverse") between a
     * {@code String}-{@link javafx.beans.property.Property} and another {@code Property}
     * using the specified {@link javafx.util.StringConverter} for conversion.
     * <p>
     * A bidirectional binding is a binding that works in both directions. If
     * two properties {@code a} and {@code b} are linked with a bidirectional
     * binding and the value of {@code a} changes, {@code b} is set to the same
     * value automatically. And vice versa, if {@code b} changes, {@code a} is
     * set to the same value.
     * <p>
     * A bidirectional binding can be removed with
     * {@link #unbindBidirectional(Object, Object)}.
     * <p>
     * Note: this implementation of a bidirectional binding behaves differently
     * from all other bindings here in two important aspects. A property that is
     * linked to another property with a bidirectional binding can still be set
     * (usually bindings would throw an exception). Secondly bidirectional
     * bindings are calculated eagerly, i.e. a bound property is updated
     * immediately.
     *
     * @param stringProperty
     *            the {@code String} {@code Property}
     * @param otherProperty
     *            the other (non-{@code String}) {@code Property}
     * @param converter
     *            the {@code StringConverter} used to convert between the properties
     * @throws NullPointerException
     *            if one of the properties or the {@code converter} is {@code null}
     * @throws IllegalArgumentException
     *            if both properties are equal
     * @since JavaFX 2.1
     */
    public static <T> void bindBidirectional(Property<String> stringProperty, Property<T> otherProperty, StringConverter<T> converter) {
        BidirectionalBinding.bind(stringProperty, otherProperty, converter);
    }

    // boolean
    // =================================================================================================================

    // Generic boolean bi operator with shortcut value
    private static abstract class BooleanBiOpBinding extends BooleanBinding {
        protected final ObservableBooleanValue op1;
        protected final ObservableBooleanValue op2;
        private final InvalidationListener observer;

        public BooleanBiOpBinding(ObservableBooleanValue op1, ObservableBooleanValue op2, boolean op1ShortcutValue) {
            if ((op1 == null) || (op2 == null)) {
                throw new NullPointerException("Operands cannot be null.");
            }
            this.op1 = op1;
            this.op2 = op2;

            observer = new ShortCircuitBooleanInvalidator(this, op1ShortcutValue);

            op1.addListener(observer);
            op2.addListener(observer);
        }

        @Override
        public void dispose() {
            op1.removeListener(observer);
            op2.removeListener(observer);
        }

        @Override
        public ObservableList<?> getDependencies() {
            return new ImmutableObservableList<>(op1, op2);
        }
    }

    private static class ShortCircuitBooleanInvalidator implements InvalidationListener {

        private final WeakReference<BooleanBiOpBinding> ref;
        private final boolean op1ShortcutValue;

        private ShortCircuitBooleanInvalidator(BooleanBiOpBinding binding, boolean op1ShortcutValue) {
            assert binding != null;
            ref = new WeakReference<>(binding);
            this.op1ShortcutValue = op1ShortcutValue;
        }

        @Override
        public void invalidated(Observable observable) {
            final BooleanBiOpBinding binding = ref.get();
            if (binding == null) {
                observable.removeListener(this);
            } else {
                // short-circuit invalidation. This BooleanBinding becomes
                // only invalid if the first operator changes or the
                // first parameter is true.
                if ((binding.op1.equals(observable) || (binding.isValid() && binding.op1.get() != op1ShortcutValue))) {
                    binding.invalidate();
                }
            }
        }
    }

    // =================================================================================================================
    // And
    private static class BooleanAndBinding extends BooleanBiOpBinding {

        public BooleanAndBinding(ObservableBooleanValue op1, ObservableBooleanValue op2) {
            super(op1, op2, false);
        }

        @Override
        protected boolean computeValue() {
            return op1.get() && op2.get();
        }

    }

    /**
     * Creates a {@link BooleanBinding} that calculates the conditional-AND
     * operation on the value of two instance of
     * {@link javafx.beans.value.ObservableBooleanValue}.
     *
     * @param op1
     *            first {@code ObservableBooleanValue}
     * @param op2
     *            second {@code ObservableBooleanValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if one of the operands is {@code null}
     */
    public static BooleanBinding and(final ObservableBooleanValue op1, final ObservableBooleanValue op2) {
        return new BooleanAndBinding(op1, op2);
    }

    // =================================================================================================================
    // Or
    private static class BooleanOrBinding extends BooleanBiOpBinding {

        public BooleanOrBinding(ObservableBooleanValue op1, ObservableBooleanValue op2) {
            super(op1, op2, true);
        }

        @Override
        protected boolean computeValue() {
            return op1.get() || op2.get();
        }

    }

    /**
     * Creates a {@link BooleanBinding} that calculates the conditional-AND
     * operation on the value of two instance of
     * {@link javafx.beans.value.ObservableBooleanValue}.
     *
     * @param op1
     *            first {@code ObservableBooleanValue}
     * @param op2
     *            second {@code ObservableBooleanValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if one of the operands is {@code null}
     */
    public static BooleanBinding or(final ObservableBooleanValue op1, final ObservableBooleanValue op2) {
        return new BooleanOrBinding(op1, op2);
    }

    // =================================================================================================================
    // Divide

    private static NumberBinding divide(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new DoubleBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected double computeValue() {
                    return op1.doubleValue() / op2.doubleValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } /*else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new FloatBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected float computeValue() {
                    return op1.floatValue() / op2.floatValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }*/ else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new LongBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected long computeValue() {
                    return op1.longValue() / op2.longValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new IntegerBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected int computeValue() {
                    return op1.intValue() / op2.intValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }
    /**
     * Creates a new {@link javafx.beans.binding.NumberBinding} that calculates
     * the division of the value of a
     * {@link javafx.beans.value.ObservableNumberValue} and a constant value.
     *
     * @param op1
     *            the constant value
     * @param op2
     *            the {@code ObservableNumberValue}
     * @return the new {@code NumberBinding}
     * @throws NullPointerException
     *             if the {@code ObservableNumberValue} is {@code null}
     */

    public static NumberBinding divide(final ObservableNumberValue op1, int op2) {
        return Bindings.divide(op1, IntegerConstant.valueOf(op2), op1);
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if a given {@link javafx.collections.ObservableList} is empty.
     *
     * @param op
     *            the {@code ObservableList}
     * @param <E> type of the {@code List} elements
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableList} is {@code null}
     * @since JavaFX 2.1
     */
    public static <E> BooleanBinding isEmpty(final ObservableList<E> op) {
        return createBooleanBinding(op::isEmpty, op);
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the value of a {@link javafx.beans.value.ObservableStringValue} is empty.
     * <p>
     * Note: In this comparison a {@code String} that is {@code null} is
     * considered to be empty.
     *
     * @param op
     *            the {@code ObservableStringValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableStringValue} is {@code null}
     * @since JavaFX 8.0
     */
    public static BooleanBinding isEmpty(final ObservableStringValue op) {
        return createBooleanBinding(() -> Strings.isEmpty(op.get()), op);
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the value of a {@link javafx.beans.value.ObservableStringValue} is not empty.
     * <p>
     * Note: In this comparison a {@code String} that is {@code null} is
     * considered to be empty.
     *
     * @param op
     *            the {@code ObservableStringValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableStringValue} is {@code null}
     * @since JavaFX 8.0
     */
    public static BooleanBinding isNotEmpty(final ObservableStringValue op) {
        return createBooleanBinding(() -> Strings.isNotEmpty(op.get()), op);
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the values of two instances of
     * {@link javafx.beans.value.ObservableObjectValue} are equal.
     *
     * @param op1
     *            the first operand
     * @param op2
     *            the second operand
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if one of the operands is {@code null}
     */
    public static BooleanBinding equal(final ObservableObjectValue<?> op1, final ObservableObjectValue<?> op2) {
        return createBooleanBinding(() -> Objects.equals(op1.get(), op2.get()), op1, op2);
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the value of an {@link javafx.beans.value.ObservableObjectValue} is
     * equal to a constant value.
     *
     * @param op1
     *            the {@code ObservableObjectValue}
     * @param op2
     *            the constant value
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableObjectValue} is {@code null}
     */
    public static BooleanBinding equal(final ObservableObjectValue<?> op1, Object op2) {
        return createBooleanBinding(() -> Objects.equals(op1.get(), op2), op1);
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the value of an {@link javafx.beans.value.ObservableObjectValue} is
     * equal to a constant value.
     *
     * @param op1
     *            the constant value
     * @param op2
     *            the {@code ObservableObjectValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableObjectValue} is {@code null}
     */
    public static BooleanBinding equal(Object op1, final ObservableObjectValue<?> op2) {
        return createBooleanBinding(() -> Objects.equals(op1, op2.get()), op2);
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the values of two instances of
     * {@link javafx.beans.value.ObservableObjectValue} are not equal.
     *
     * @param op1
     *            the first operand
     * @param op2
     *            the second operand
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if one of the operands is {@code null}
     */
    public static BooleanBinding notEqual(final ObservableObjectValue<?> op1, final ObservableObjectValue<?> op2) {
        return equal(op1, op2).not();
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the value of an {@link javafx.beans.value.ObservableObjectValue} is
     * not equal to a constant value.
     *
     * @param op1
     *            the {@code ObservableObjectValue}
     * @param op2
     *            the constant value
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableObjectValue} is {@code null}
     */
    public static BooleanBinding notEqual(final ObservableObjectValue<?> op1, Object op2) {
        return equal(op1, op2).not();
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the value of an {@link javafx.beans.value.ObservableObjectValue} is
     * not equal to a constant value.
     *
     * @param op1
     *            the constant value
     * @param op2
     *            the {@code ObservableObjectValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableObjectValue} is {@code null}
     */
    public static BooleanBinding notEqual(Object op1, final ObservableObjectValue<?> op2) {
        return equal(op1, op2).not();
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the value of an {@link javafx.beans.value.ObservableObjectValue} is
     * {@code null}.
     *
     * @param op
     *            the {@code ObservableObjectValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableObjectValue} is {@code null}
     */
    public static BooleanBinding isNull(final ObservableObjectValue<?> op) {
        return createBooleanBinding(() -> op.get() == null, op);
    }

    /**
     * Creates a new {@link javafx.beans.binding.BooleanBinding} that holds {@code true}
     * if the value of an {@link javafx.beans.value.ObservableObjectValue} is
     * not {@code null}.
     *
     * @param op
     *            the {@code ObservableObjectValue}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if the {@code ObservableObjectValue} is {@code null}
     */
    public static BooleanBinding isNotNull(final ObservableObjectValue<?> op) {
        return isNull(op).not();
    }

}
