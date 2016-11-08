package naga.providers.toolkit.javafx.events;

import naga.toolkit.spi.events.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class FxActionEvent extends FxEvent<javafx.event.ActionEvent> implements ActionEvent {

    public FxActionEvent(javafx.event.ActionEvent fxEvent) {
        super(fxEvent);
    }
}
