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

package emul.javafx.beans.binding;

import emul.javafx.beans.value.ObservableStringValue;
import emul.javafx.beans.value.ObservableValue;



/**
 * A {@code StringExpression} is a
 * {@link emul.javafx.beans.value.ObservableStringValue} plus additional convenience
 * methods to generate bindings in a fluent style.
 * <p>
 * A concrete sub-class of {@code StringExpression} has to implement the method
 * {@link emul.javafx.beans.value.ObservableStringValue#get()}, which provides the
 * actual value of this expression.
 * <p>
 * Note: all implementation of {@link emul.javafx.beans.binding.BooleanBinding}
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

    // removed
}
