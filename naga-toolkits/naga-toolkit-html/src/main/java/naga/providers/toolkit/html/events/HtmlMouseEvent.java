package naga.providers.toolkit.html.events;

import elemental2.Event;
import naga.toolkit.spi.events.MouseEvent;

/**
 * @author Bruno Salmon
 */
public class HtmlMouseEvent extends HtmlEvent<Event> implements MouseEvent {

    public HtmlMouseEvent(Event htmlEvent) {
        super(htmlEvent);
    }
}
