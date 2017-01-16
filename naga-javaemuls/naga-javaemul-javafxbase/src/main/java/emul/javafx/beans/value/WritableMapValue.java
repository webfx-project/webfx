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

import emul.javafx.collections.ObservableMap;

/**
 * A writable reference to an {@link emul.javafx.collections.ObservableMap}.
 *
 * @see emul.javafx.collections.ObservableMap
 * @see emul.javafx.beans.value.WritableObjectValue
 * @see emul.javafx.beans.value.WritableMapValue
 *
 * @param <K> the type of the key elements of the {@code Map}
 * @param <V> the type of the value elements of the {@code Map}
 * @since JavaFX 2.1
 */
public interface WritableMapValue<K, V> extends WritableObjectValue<ObservableMap<K,V>>, ObservableMap<K, V> {
}
