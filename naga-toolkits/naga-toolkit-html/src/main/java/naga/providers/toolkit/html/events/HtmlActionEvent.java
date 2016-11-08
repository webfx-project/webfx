package naga.providers.toolkit.html.events;

import elemental2.Event;
import naga.toolkit.spi.events.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class HtmlActionEvent extends HtmlEvent<Event> implements ActionEvent {

    public HtmlActionEvent(Event clickEvent) {
        super(clickEvent);
    }
}
