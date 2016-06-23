package naga.core.spi.toolkit.javafx.events;

import naga.core.spi.toolkit.events.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class FxActionEvent implements ActionEvent {

    private final javafx.event.ActionEvent fxActionEvent;

    public FxActionEvent(javafx.event.ActionEvent fxActionEvent) {
        this.fxActionEvent = fxActionEvent;
    }
}
