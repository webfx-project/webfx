package naga.providers.toolkit.javafx.events;

import naga.toolkit.spi.events.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class FxActionEvent implements ActionEvent {

    private final javafx.event.ActionEvent fxActionEvent;

    public FxActionEvent(javafx.event.ActionEvent fxActionEvent) {
        this.fxActionEvent = fxActionEvent;
    }
}
