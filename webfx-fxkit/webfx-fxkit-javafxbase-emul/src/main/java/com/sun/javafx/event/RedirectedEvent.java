package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

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

