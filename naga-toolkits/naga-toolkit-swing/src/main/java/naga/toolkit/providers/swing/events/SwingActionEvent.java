package naga.toolkit.providers.swing.events;

import naga.toolkit.spi.events.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class SwingActionEvent implements ActionEvent {

    private final java.awt.event.ActionEvent event;

    public SwingActionEvent(java.awt.event.ActionEvent event) {
        this.event = event;
    }
}
