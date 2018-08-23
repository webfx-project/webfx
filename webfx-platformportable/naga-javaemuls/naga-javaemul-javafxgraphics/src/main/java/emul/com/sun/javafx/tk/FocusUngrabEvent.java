package emul.com.sun.javafx.tk;

import emul.javafx.event.Event;
import emul.javafx.event.EventTarget;
import emul.javafx.event.EventType;

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
