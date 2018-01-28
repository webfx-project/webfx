package naga.fx.properties;

import emul.javafx.beans.value.ChangeListener;
import emul.javafx.beans.value.ObservableValue;
import naga.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class UnregistrableListener implements Unregistrable {

    private final ChangeListener changeListener;
    private final ObservableValue[] observableValues;
    private boolean registered;

    public UnregistrableListener(ChangeListener changeListener, ObservableValue... observableValues) {
        this.changeListener = changeListener;
        this.observableValues = observableValues;
        register();
    }

    public UnregistrableListener(Consumer<ObservableValue> runnable, ObservableValue... observableValues) {
        this((observable, oldValue, newValue) -> runnable.accept(observable), observableValues);
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