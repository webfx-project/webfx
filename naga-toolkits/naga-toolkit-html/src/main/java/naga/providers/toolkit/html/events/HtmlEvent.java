package naga.providers.toolkit.html.events;

import elemental2.Event;
import naga.toolkit.spi.events.UiEvent;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlEvent<E extends Event> implements UiEvent {

    private final E htmlEvent;

    public HtmlEvent(E htmlEvent) {
        this.htmlEvent = htmlEvent;
    }
}
