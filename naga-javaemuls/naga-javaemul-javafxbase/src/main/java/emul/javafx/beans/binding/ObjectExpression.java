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


import emul.javafx.beans.value.ObservableObjectValue;
import emul.javafx.collections.FXCollections;
import emul.javafx.collections.ObservableList;



/**
 * A {@code ObjectExpression} is a
 * {@link emul.javafx.beans.value.ObservableObjectValue} plus additional convenience
 * methods to generate bindings in a fluent style.
 * <p>
 * A concrete sub-class of {@code ObjectExpression} has to implement the method
 * {@link emul.javafx.beans.value.ObservableObjectValue#get()}, which provides the
 * actual value of this expression.
 * @since JavaFX 2.0
 */
public abstract class ObjectExpression<T> implements ObservableObjectValue<T> {

    @Override
    public T getValue() {
        return get();
    }

    /**
     * Returns an {@code ObjectExpression} that wraps an
     * {@link emul.javafx.beans.value.ObservableObjectValue}. If the
     * {@code ObservableObjectValue} is already an {@code ObjectExpression}, it
     * will be returned. Otherwise a new
     * {@link emul.javafx.beans.binding.ObjectBinding} is created that is bound to
     * the {@code ObservableObjectValue}.
     *
     * @param <T> the type of the wrapped {@code Object}
     * @param value
     *            The source {@code ObservableObjectValue}
     * @return A {@code ObjectExpression} that wraps the
     *         {@code ObservableObjectValue} if necessary
     * @throws NullPointerException
     *             if {@code value} is {@code null}
     */
    // removed

    /**
     * Creates a new {@code BooleanExpression} that holds {@code true} if this and
     * another {@link emul.javafx.beans.value.ObservableObjectValue} are equal.
     *
     * @param other
     *            the other {@code ObservableObjectValue}
     * @return the new {@code BooleanExpression}
     * @throws NullPointerException
     *             if {@code other} is {@code null}
     */
    // removed

    /**
     * Creates a new {@code BooleanExpression} that holds {@code true} if this
     * {@code ObjectExpression} is equal to a constant value.
     *
     * @param other
     *            the constant value
     * @return the new {@code BooleanExpression}
     */
    // removed

    /**
     * Creates a new {@code BooleanExpression} that holds {@code true} if this and
     * another {@link emul.javafx.beans.value.ObservableObjectValue} are not equal.
     *
     * @param other
     *            the other {@code ObservableObjectValue}
     * @return the new {@code BooleanExpression}
     * @throws NullPointerException
     *             if {@code other} is {@code null}
     */
    // removed

    /**
     * Creates a new {@code BooleanExpression} that holds {@code true} if this
     * {@code ObjectExpression} is not equal to a constant value.
     *
     * @param other
     *            the constant value
     * @return the new {@code BooleanExpression}
     */
    // removed

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if this
     * {@code ObjectExpression} is {@code null}.
     *
     * @return the new {@code BooleanBinding}
     */
    // removed

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if this
     * {@code ObjectExpression} is not {@code null}.
     *
     * @return the new {@code BooleanBinding}
     */
    // removed

    /**
     * Creates a {@link emul.javafx.beans.binding.StringBinding} that holds the value
     * of this {@code ObjectExpression} turned into a {@code String}. If the
     * value of this {@code ObjectExpression} changes, the value of the
     * {@code StringBinding} will be updated automatically.
     *
     * @return the new {@code StringBinding}
     * @since JavaFX 8.0
     */
    // removed

    /**
     * Creates a {@link emul.javafx.beans.binding.StringBinding} that holds the value
     * of the {@code ObjectExpression} turned into a {@code String}. If the
     * value of this {@code ObjectExpression} changes, the value of the
     * {@code StringBinding} will be updated automatically.
     * <p>
     * The result is formatted according to the formatting {@code String}. See
     * {@code java.util.Formatter} for formatting rules.
     *
     * @param format
     *            the formatting {@code String}
     * @return the new {@code StringBinding}
     * @since JavaFX 8.0
     */
    // removed

    /**
     * Creates a {@link emul.javafx.beans.binding.StringBinding} that holds the value
     * of the {@code NumberExpression} turned into a {@code String}. If the
     * value of this {@code NumberExpression} changes, the value of the
     * {@code StringBinding} will be updated automatically.
     * <p>
     * The result is formatted according to the formatting {@code String} and
     * the passed in {@code Locale}. See {@code java.util.Formatter} for
     * formatting rules. See {@code java.util.Locale} for details on
     * {@code Locale}.
     *
     * @param locale the Locale to be used
     * @param format
     *            the formatting {@code String}
     * @return the new {@code StringBinding}
     * @since JavaFX 8.0
     */
    // removed
}
