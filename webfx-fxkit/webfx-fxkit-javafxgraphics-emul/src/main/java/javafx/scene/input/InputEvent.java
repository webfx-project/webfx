package javafx.scene.input;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * An event indicating a user input.
 * @since JavaFX 2.0
 */
public class InputEvent extends Event {

    //private static final long serialVersionUID = 20121107L;

    /**
     * Common supertype for all input event types.
     */
    public static final EventType<InputEvent> ANY = new EventType<InputEvent> (Event.ANY, "INPUT");

    /**
     * Creates new instance of InputEvent.
     * @param eventType Type of the event
     */
    public InputEvent(EventType<? extends InputEvent> eventType) {
        super(eventType);
    }

    /**
     * Creates new instance of InputEvent.
     * @param source Event source
     * @param target Event target
     * @param eventType Type of the event
     */
    public InputEvent(Object source, EventTarget target, EventType<? extends InputEvent> eventType) {
        super(source, target, eventType);
    }

    @Override
    public EventType<? extends InputEvent> getEventType() {
        return (EventType<? extends InputEvent>) super.getEventType();
    }
}
