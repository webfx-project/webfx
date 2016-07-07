package naga.toolkit.providers.gwt.events;

import com.google.gwt.event.dom.client.ClickEvent;
import naga.toolkit.spi.events.ActionEvent;

/**
 * @author Bruno Salmon
 */
public class GwtActionEvent implements ActionEvent {

    private final ClickEvent clickEvent;

    public GwtActionEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }
}
