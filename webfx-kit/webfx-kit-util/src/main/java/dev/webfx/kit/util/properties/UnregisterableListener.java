package dev.webfx.kit.util.properties;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class UnregisterableListener implements Unregisterable {

    private final ChangeListener changeListener;
    private final ObservableValue[] observableValues;
    private boolean registered;

    public UnregisterableListener(ChangeListener changeListener, ObservableValue... observableValues) {
        this.changeListener = changeListener;
        this.observableValues = observableValues;
        register();
    }

    public UnregisterableListener(Consumer<ObservableValue> propertyConsumer, ObservableValue... observableValues) {
        this((observable, oldValue, newValue) -> propertyConsumer.accept(observable), observableValues);
    }

    @Override
    public void register() {
        if (!registered) {
            for (ObservableValue observableValue : observableValues)
                if (observableValue != null)
                    observableValue.addListener(changeListener);
            registered = true;
        }
    }

    @Override
    public void unregister() {
        if (registered) {
            for (ObservableValue observableValue : observableValues)
                if (observableValue != null)
                    observableValue.removeListener(changeListener);
            registered = false;
        }
    }
}
