package javafx.collections;

import javafx.beans.Observable;

import java.util.Map;

/**
 * A map that allows observers to track changes when they occur.
 *
 * @see MapChangeListener
 * @see MapChangeListener.Change
 * @since JavaFX 2.0
 */
public interface ObservableMap<K, V> extends Map<K, V>, Observable {
    /**
     * Add a listener to this observable map.
     * @param listener the listener for listening to the list changes
     */
    void addListener(MapChangeListener<? super K, ? super V> listener);
    /**
     * Tries to removed a listener from this observable map. If the listener is not
     * attached to this map, nothing happens.
     * @param listener a listener to remove
     */
    void removeListener(MapChangeListener<? super K, ? super V> listener);
}
