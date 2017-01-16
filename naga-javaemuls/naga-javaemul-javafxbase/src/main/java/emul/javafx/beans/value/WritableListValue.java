/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
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

package emul.javafx.beans.value;

import emul.javafx.collections.ObservableList;

/**
 * A writable reference to an {@link emul.javafx.collections.ObservableList}.
 *
 * @see emul.javafx.collections.ObservableList
 * @see emul.javafx.beans.value.WritableObjectValue
 * @see emul.javafx.beans.value.WritableListValue
 *
 * @param <E> the type of the {@code List} elements
 * @since JavaFX 2.1
 */
public interface WritableListValue<E> extends WritableObjectValue<ObservableList<E>>, ObservableList<E> {
}
