package emul.com.sun.javafx.event;

import emul.javafx.event.Event;
import emul.javafx.event.EventTarget;
import emul.javafx.event.EventType;

/**
 * Used as a wrapper to protect an {@code Event} from being redirected by
 * {@code EventRedirector}. The redirector only unwraps such event and sends
 * it to the rest of the event chain.
 */
public class DirectEvent extends Event {
    //private static final long serialVersionUID = 20121107L;

    public static final EventType<DirectEvent> DIRECT = new EventType<>(Event.ANY, "DIRECT");

    private final Event originalEvent;

    public DirectEvent(Event originalEvent) {
        this(originalEvent, null, null);
    }

    public DirectEvent(Event originalEvent, Object source, EventTarget target) {
        super(source, target, DIRECT);
        this.originalEvent = originalEvent;
    }

    public Event getOriginalEvent() {
        return originalEvent;
    }
}
