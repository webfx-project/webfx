package com.sun.javafx.scene.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

public final class MultiplePropertyChangeListenerHandler {

    private final Callback<String, Void> propertyChangedHandler;

    public MultiplePropertyChangeListenerHandler(Callback<String, Void> propertyChangedHandler) {
        this.propertyChangedHandler = propertyChangedHandler;
    }

    /**
     * This is part of the workaround introduced during delomboking. We probably will
     * want to adjust the way listeners are added rather than continuing to use this
     * map (although it doesn't really do much harm).
     */
    private Map<ObservableValue<?>,String> propertyReferenceMap =
            new HashMap<ObservableValue<?>,String>();

    private final ChangeListener<Object> propertyChangedListener = new ChangeListener<Object>() {
        @Override public void changed(ObservableValue<?> property,
                                      @SuppressWarnings("unused") Object oldValue,
                                      @SuppressWarnings("unused") Object newValue) {
            propertyChangedHandler.call(propertyReferenceMap.get(property));
        }
    };

    private final WeakChangeListener<Object> weakPropertyChangedListener =
            new WeakChangeListener<Object>(propertyChangedListener);

    /**
     * Subclasses can invoke this method to register that we want to listen to
     * property change events for the given property.
     *
     * @param property
     * @param reference
     */
    public final void registerChangeListener(ObservableValue<?> property, String reference) {
        if (!propertyReferenceMap.containsKey(property)) {
            propertyReferenceMap.put(property, reference);
            property.addListener(weakPropertyChangedListener);
        }
    }

    public final void unregisterChangeListener(ObservableValue<?> property) {
        if (propertyReferenceMap.containsKey(property)) {
            propertyReferenceMap.remove(property);
            property.removeListener(weakPropertyChangedListener);
        }
    }

    public void dispose() {
        // unhook listeners
        for (ObservableValue<?> value : propertyReferenceMap.keySet()) {
            value.removeListener(weakPropertyChangedListener);
        }
        propertyReferenceMap.clear();
    }
}

