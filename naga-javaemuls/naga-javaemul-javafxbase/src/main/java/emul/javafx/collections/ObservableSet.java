package emul.javafx.collections;

import emul.javafx.beans.Observable;

import java.util.Set;

/**
 * A set that allows observers to track changes when they occur.
 *
 * @see SetChangeListener
 * @see SetChangeListener.Change
 * @since JavaFX 2.1
 */
public interface ObservableSet<E> extends Set<E>, Observable {
    /**
     * Add a listener to this observable set.
     * @param listener the listener for listening to the set changes
     */
    public void addListener(SetChangeListener<? super E> listener);
    /**
     * Tries to removed a listener from this observable set. If the listener is not
     * attached to this list, nothing happens.
     * @param listener a listener to remove
     */
    public void removeListener(SetChangeListener<? super E> listener);
}
