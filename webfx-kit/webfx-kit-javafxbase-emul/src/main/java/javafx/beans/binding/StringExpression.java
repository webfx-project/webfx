/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
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

import javafx.beans.value.ObservableStringValue;


/**
 * A {@code StringExpression} is a
 * {@link javafx.beans.value.ObservableStringValue} plus additional convenience
 * methods to generate bindings in a fluent style.
 * <p>
 * A concrete sub-class of {@code StringExpression} has to implement the method
 * {@link javafx.beans.value.ObservableStringValue#get()}, which provides the
 * actual value of this expression.
 * <p>
 * Note: all implementation of {@link javafx.beans.binding.BooleanBinding}
 * returned by the comparisons in this class consider a {@code String} that is
 * {@code null} equal to an empty {@code String}.
 * @since JavaFX 2.0
 */
public abstract class StringExpression implements ObservableStringValue {

    @Override
    public String getValue() {
        return get();
    }

    /**
     * Returns usually the value of this {@code StringExpression}. Only if the
     * value is {@code null} an empty {@code String} is returned instead.
     *
     * @return the value of this {@code StringExpression} or the empty
     *         {@code String}
     */
    public final String getValueSafe() {
        final String value = get();
        return value == null ? "" : value;
    }

    public BooleanBinding isEmpty() {
        return Bindings.isEmpty(this);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if this
     * {@code StringExpression} is not empty.
     * <p>
     * Note: If the value of this {@code StringExpression} is {@code null},
     * it is considered to be empty.
     *
     * @return the new {@code BooleanBinding}
     * @since JavaFX 8.0
     */
    public BooleanBinding isNotEmpty() {
        return Bindings.isNotEmpty(this);
    }

}
