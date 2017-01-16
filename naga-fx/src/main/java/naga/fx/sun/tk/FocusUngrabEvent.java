package naga.fx.sun.tk;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class FocusUngrabEvent extends Event {
    //private static final long serialVersionUID = 20121107L;

    public static final EventType<FocusUngrabEvent> FOCUS_UNGRAB =
            new EventType<FocusUngrabEvent>(Event.ANY, "FOCUS_UNGRAB");

    public static final EventType<FocusUngrabEvent> ANY = FOCUS_UNGRAB;

    public FocusUngrabEvent() {
        super(FOCUS_UNGRAB);
    }

    public FocusUngrabEvent(final Object source,
                            final EventTarget target) {
        super(source, target, FOCUS_UNGRAB);
    }
}
