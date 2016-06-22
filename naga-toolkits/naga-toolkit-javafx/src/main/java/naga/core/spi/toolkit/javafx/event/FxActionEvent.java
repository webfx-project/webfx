package naga.core.spi.toolkit.javafx.event;

import naga.core.spi.toolkit.event.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class FxActionEvent implements ActionEvent {

    private final javafx.event.ActionEvent fxActionEvent;

    public FxActionEvent(javafx.event.ActionEvent fxActionEvent) {
        this.fxActionEvent = fxActionEvent;
    }
}
