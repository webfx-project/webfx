package com.sun.javafx.event;

import dev.webfx.platform.util.tuples.Pair;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An {@code EventDispatcher} which allows user event handler / filter
 * registration and when used in an event dispatch chain it forwards received
 * events to the appropriate registered handlers / filters.
 */
public class EventHandlerManager extends BasicEventDispatcher {
    private final Map<EventType<? extends Event>,
            CompositeEventHandler<? extends Event>> eventHandlerMap;

    private final Object eventSource;

    public EventHandlerManager(final Object eventSource) {
        this.eventSource = eventSource;
        eventHandlerMap = new HashMap<>();
    }

    /**
     * Registers an event handler in {@code EventHandlerManager}.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the type of the events to receive by the handler
     * @param eventHandler the handler to register
     * @throws NullPointerException if the event type or handler is null
     */
    public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        validateEventType(eventType);
        validateEventHandler(eventHandler);

        CompositeEventHandler<T> compositeEventHandler = createGetCompositeEventHandler(eventType);
        compositeEventHandler.addEventHandler(eventHandler);

        notifyEventSourcesListener(eventType, eventSource); // WebFX addition
    }

    /**
     * Unregisters a previously registered event handler.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the event type from which to unregister
     * @param eventHandler the handler to unregister
     * @throws NullPointerException if the event type or handler is null
     */
    public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        validateEventType(eventType);
        validateEventHandler(eventHandler);

        CompositeEventHandler<T> compositeEventHandler = (CompositeEventHandler<T>) eventHandlerMap.get(eventType);
        if (compositeEventHandler != null)
            compositeEventHandler.removeEventHandler(eventHandler);
    }

    /**
     * Registers an event filter in {@code EventHandlerManager}.
     *
     * @param <T> the specific event class of the filter
     * @param eventType the type of the events to receive by the filter
     * @param eventFilter the filter to register
     * @throws NullPointerException if the event type or filter is null
     */
    public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        validateEventType(eventType);
        validateEventFilter(eventFilter);

        CompositeEventHandler<T> compositeEventHandler = createGetCompositeEventHandler(eventType);
        compositeEventHandler.addEventFilter(eventFilter);

        notifyEventSourcesListener(eventType, eventSource); // WebFX addition
    }

    /**
     * Unregisters a previously registered event filter.
     *
     * @param <T> the specific event class of the filter
     * @param eventType the event type from which to unregister
     * @param eventFilter the filter to unregister
     * @throws NullPointerException if the event type or filter is null
     */
    public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        validateEventType(eventType);
        validateEventFilter(eventFilter);

        CompositeEventHandler<T> compositeEventHandler = (CompositeEventHandler<T>) eventHandlerMap.get(eventType);
        if (compositeEventHandler != null)
            compositeEventHandler.removeEventFilter(eventFilter);
    }

    /**
     * Sets the specified singleton handler. There can only be one such handler
     * specified at a time.
     *
     * @param <T> the specific event class of the handler
     * @param eventType the event type to associate with the given eventHandler
     * @param eventHandler the handler to register, or null to unregister
     * @throws NullPointerException if the event type is null
     */
    public final <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        validateEventType(eventType);

        CompositeEventHandler<T> compositeEventHandler = (CompositeEventHandler<T>) eventHandlerMap.get(eventType);

        if (compositeEventHandler == null) {
            if (eventHandler == null)
                return;
            compositeEventHandler = new CompositeEventHandler<T>();
            eventHandlerMap.put(eventType, compositeEventHandler);
        }

        compositeEventHandler.setEventHandler(eventHandler);

        notifyEventSourcesListener(eventType, eventSource); // WebFX addition
    }

    public final <T extends Event> EventHandler<? super T> getEventHandler(EventType<T> eventType) {
        CompositeEventHandler<T> compositeEventHandler = (CompositeEventHandler<T>) eventHandlerMap.get(eventType);

        return (compositeEventHandler != null)
                ? compositeEventHandler.getEventHandler()
                : null;
    }

    @Override
    public final Event dispatchCapturingEvent(Event event) {
        EventType<? extends Event> eventType = event.getEventType();
        do {
            event = dispatchCapturingEvent(eventType, event);
            eventType = eventType.getSuperType();
        } while (eventType != null);

        return event;
    }

    @Override
    public final Event dispatchBubblingEvent(Event event) {
        EventType<? extends Event> eventType = event.getEventType();
        do {
            event = dispatchBubblingEvent(eventType, event);
            eventType = eventType.getSuperType();
        } while (eventType != null);

        return event;
    }

    private <T extends Event> CompositeEventHandler<T>
    createGetCompositeEventHandler(final EventType<T> eventType) {
        CompositeEventHandler<T> compositeEventHandler =
                (CompositeEventHandler<T>) eventHandlerMap.get(eventType);
        if (compositeEventHandler == null) {
            compositeEventHandler = new CompositeEventHandler<T>();
            eventHandlerMap.put(eventType, compositeEventHandler);
        }

        return compositeEventHandler;
    }

    protected Object getEventSource() {
        return eventSource;
    }

    private Event dispatchCapturingEvent(
            final EventType<? extends Event> handlerType, Event event) {
        final CompositeEventHandler<? extends Event> compositeEventHandler =
                eventHandlerMap.get(handlerType);

        if (compositeEventHandler != null) {
            // TODO: skip when no filters are registered in the
            //       CompositeEventHandler (RT-23952)
            event = fixEventSource(event, eventSource);
            compositeEventHandler.dispatchCapturingEvent(event);
        }

        return event;
    }

    private Event dispatchBubblingEvent(
            final EventType<? extends Event> handlerType, Event event) {
        final CompositeEventHandler<? extends Event> compositeEventHandler =
                eventHandlerMap.get(handlerType);

        if (compositeEventHandler != null) {
            // TODO: skip when no handlers are registered in the
            //       CompositeEventHandler (RT-23952)
            event = fixEventSource(event, eventSource);
            compositeEventHandler.dispatchBubblingEvent(event);
        }

        return event;
    }

    private static Event fixEventSource(final Event event,
                                        final Object eventSource) {
        return (event.getSource() != eventSource)
                ? event.copyFor(eventSource, event.getTarget())
                : event;
    }

    private static void validateEventType(final EventType<?> eventType) {
        if (eventType == null) {
            throw new NullPointerException("Event type must not be null");
        }
    }

    private static void validateEventHandler(
            final EventHandler<?> eventHandler) {
        if (eventHandler == null) {
            throw new NullPointerException("Event handler must not be null");
        }
    }

    private static void validateEventFilter(
            final EventHandler<?> eventFilter) {
        if (eventFilter == null) {
            throw new NullPointerException("Event filter must not be null");
        }
    }

    // WebFX addition

    /* EventSourcesListener is used by HtmlSvgNodePeer to be notified when an event handler or filter is added to a node,
    * so it can add an appropriate HTML event handler on the HTML peer. */
    public interface EventSourcesListener {
        void onEventSource(EventType<?> eventType, Object eventSource);
    }
    private static EventSourcesListener EVENT_SOURCES_LISTENER;
    private static final List<Pair<EventType<?>, Object>> PENDING_NOTIFY_EVENTS = new ArrayList<>();

    private static void notifyEventSourcesListener(EventType<?> eventType, Object eventSource) {
        if (EVENT_SOURCES_LISTENER != null) {
            EVENT_SOURCES_LISTENER.onEventSource(eventType, eventSource);
        } else {
            PENDING_NOTIFY_EVENTS.add(new Pair<>(eventType, eventSource));
        }
    }

    public static void setEventSourcesListener(EventSourcesListener eventSourcesListener) {
        EVENT_SOURCES_LISTENER = eventSourcesListener;
        PENDING_NOTIFY_EVENTS.forEach(pair -> eventSourcesListener.onEventSource(pair.get1(), pair.get2()));
        PENDING_NOTIFY_EVENTS.clear();
    }

}
