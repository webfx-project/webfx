package naga.providers.toolkit.swing.events;

import naga.toolkit.spi.events.MouseEvent;

/**
 * @author Bruno Salmon
 */
public class SwingMouseEvent extends SwingEvent<java.awt.event.MouseEvent> implements MouseEvent {

    public SwingMouseEvent(java.awt.event.MouseEvent swEvent) {
        super(swEvent);
    }
}
