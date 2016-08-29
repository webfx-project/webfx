package naga.providers.toolkit.html.events;

import elemental2.Event;
import naga.toolkit.spi.events.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class HtmlActionEvent implements ActionEvent {

    private final Event clickEvent;

    public HtmlActionEvent(Event clickEvent) {
        this.clickEvent = clickEvent;
    }
}
