package dev.webfx.kit.mapper.peers.javafxweb.engine;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;

/**
 * @author Bruno Salmon
 */
public class WorkerImpl<V> implements Worker<V> {

    private final ObjectProperty<State> stateProperty = new SimpleObjectProperty<>(State.READY);

    @Override
    public State getState() {
        return stateProperty.get();
    }

    @Override
    public ReadOnlyObjectProperty<State> stateProperty() {
        return stateProperty;
    }

    public void setState(State state) {
        stateProperty.set(state);
    }

}
