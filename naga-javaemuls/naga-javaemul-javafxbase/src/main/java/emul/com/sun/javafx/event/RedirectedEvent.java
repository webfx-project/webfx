package emul.com.sun.javafx.event;

import emul.javafx.event.Event;
import emul.javafx.event.EventTarget;
import emul.javafx.event.EventType;

/**
 * Used as a wrapper in {@code EventRedirector} to distinquish between normal
 * "direct" events and the events "redirected" from the parent dispatcher(s).
 */
public class RedirectedEvent extends Event {

    //private static final long serialVersionUID = 20121107L;

    public static final EventType<RedirectedEvent> REDIRECTED = new EventType<>(Event.ANY, "REDIRECTED");

    private final Event originalEvent;

    public RedirectedEvent(Event originalEvent) {
        this(originalEvent, null, null);
    }

    public RedirectedEvent(Event originalEvent, Object source, EventTarget target) {
        super(source, target, REDIRECTED);
        this.originalEvent = originalEvent;
    }

    public Event getOriginalEvent() {
        return originalEvent;
    }
}

