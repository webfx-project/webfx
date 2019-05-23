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
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

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
        if (op == null) {
            throw new NullPointerException("Operand cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return !op.get();
            }

            @Override
            //@ReturnsUnmodifiableCollection
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
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


}
