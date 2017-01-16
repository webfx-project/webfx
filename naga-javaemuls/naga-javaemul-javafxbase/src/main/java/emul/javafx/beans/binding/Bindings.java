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

package emul.javafx.beans.binding;

import emul.javafx.beans.property.Property;
import emul.com.sun.javafx.binding.BidirectionalBinding;

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
 * the operands is an Expression (see {@link emul.javafx.beans.binding}). (Every
 * Expression contains a static method that generates an Expression from an
 * {@link emul.javafx.beans.value.ObservableValue}.)
 * <p>
 * Also if you watched closely, you might have noticed that the return type of
 * the Fluent API is different in the examples above. In a lot of cases the
 * Fluent API allows to be more specific about the returned type (see
 * {@link emul.javafx.beans.binding.NumberExpression} for more details about implicit
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


}
