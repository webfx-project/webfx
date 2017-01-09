package naga.fx.sun.stage;

import naga.fx.stage.Window;
import naga.fx.stage.WindowEvent;
import naga.fx.sun.event.BasicEventDispatcher;
import naga.fx.event.Event;

public final class WindowCloseRequestHandler extends BasicEventDispatcher {
    private final Window window;

    public WindowCloseRequestHandler(final Window window) {
        this.window = window;
    }

    @Override
    public Event dispatchBubblingEvent(Event event) {
        if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
            // the close request hasn't been handled by any user handler,
            // we take care of it here
            window.hide();
            event.consume();
        }

        return event;
    }
}

