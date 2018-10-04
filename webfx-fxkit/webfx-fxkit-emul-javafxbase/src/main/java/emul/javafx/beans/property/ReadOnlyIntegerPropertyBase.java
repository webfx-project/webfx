package emul.javafx.beans.property;

import emul.com.sun.javafx.binding.ExpressionHelper;
import emul.javafx.beans.InvalidationListener;
import emul.javafx.beans.value.ChangeListener;

/**
 * Base class for all readonly properties wrapping an {@code int}. This class provides a default
 * implementation to attach listener.
 *
 * @see ReadOnlyIntegerProperty
 * @since JavaFX 2.0
 */
public abstract class ReadOnlyIntegerPropertyBase extends ReadOnlyIntegerProperty {

    ExpressionHelper<Number> helper;

    @Override
    public void addListener(InvalidationListener listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(ChangeListener<? super Number> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Number> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    /**
     * Sends notifications to all attached
     * {@link javafx.beans.InvalidationListener InvalidationListeners} and
     * {@link javafx.beans.value.ChangeListener ChangeListeners}.
     *
     * This method needs to be called, if the value of this property changes.
     */
    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(helper);
    }

}
