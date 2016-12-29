package naga.fx.spi.javafx.events;

import javafx.event.Event;

/**
 * @author Bruno Salmon
 */
public abstract class FxEvent<E extends Event> {

    protected final E fxEvent;

    public FxEvent(E fxEvent) {
        this.fxEvent = fxEvent;
    }
}
