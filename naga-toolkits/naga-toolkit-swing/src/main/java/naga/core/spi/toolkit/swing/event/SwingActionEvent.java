package naga.core.spi.toolkit.swing.event;

import naga.core.spi.toolkit.event.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class SwingActionEvent implements ActionEvent {

    private final java.awt.event.ActionEvent event;

    public SwingActionEvent(java.awt.event.ActionEvent event) {
        this.event = event;
    }
}
