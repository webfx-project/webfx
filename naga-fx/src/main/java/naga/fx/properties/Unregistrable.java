package naga.fx.properties;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class Unregistrable {

    private final ChangeListener changeListeners;
    private final ObservableValue[] observableValues;
    private boolean registered;

    public Unregistrable(ChangeListener changeListeners, ObservableValue... observableValues) {
        this.changeListeners = changeListeners;
        this.observableValues = observableValues;
        register();
    }

    public Unregistrable(Consumer<ObservableValue> propertyConsumer, ObservableValue... observableValues) {
        this((observable, oldValue, newValue) -> propertyConsumer.accept(observable), observableValues);
    }

    public void register() {
        if (!registered) {
            for (ObservableValue observableValue : observableValues)
                if (observableValue != null)
                    observableValue.addListener(changeListeners);
            registered = true;
        }
    }

    public void unregister() {
        if (registered) {
            for (ObservableValue observableValue : observableValues)
                if (observableValue != null)
                    observableValue.removeListener(changeListeners);
            registered = false;
        }
    }
}
