package naga.providers.toolkit.swing.events;

import naga.toolkit.spi.events.UiEvent;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingEvent<E extends AWTEvent> implements UiEvent {

    protected final E swEvent;

    public SwingEvent(E swEvent) {
        this.swEvent = swEvent;
    }
}
