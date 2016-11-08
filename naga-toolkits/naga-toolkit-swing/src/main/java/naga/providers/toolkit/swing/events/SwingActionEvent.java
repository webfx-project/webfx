package naga.providers.toolkit.swing.events;

import naga.toolkit.spi.events.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class SwingActionEvent extends SwingEvent<java.awt.event.ActionEvent> implements ActionEvent {

    public SwingActionEvent(java.awt.event.ActionEvent swEvent) {
        super(swEvent);
    }
}
