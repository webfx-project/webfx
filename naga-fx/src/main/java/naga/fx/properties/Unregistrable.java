package naga.fx.properties;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.commons.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class Unregistrable {

    private final ChangeListener changeListeners;
    private final ObservableValue[] observableValues;
    private boolean registered;

    public Unregistrable(ChangeListener changeListeners, ObservableValue[] observableValues) {
        this.changeListeners = changeListeners;
        this.observableValues = observableValues;
        register();
    }

    public Unregistrable(Consumer<ObservableValue> runnable, ObservableValue[] observableValues) {
        this((observable, oldValue, newValue) -> runnable.accept(observable), observableValues);
    }

    public void register() {
        if (!registered) {
            for (ObservableValue observableValue : observableValues)
                observableValue.addListener(changeListeners);
            registered = true;
        }
    }

    public void unregister() {
        if (registered) {
            for (ObservableValue observableValue : observableValues)
                observableValue.removeListener(changeListeners);
            registered = false;
        }
    }
}
