package mongoose.activities.monitor.listener;

import mongoose.activities.monitor.metrics.model.MemData;
import naga.platform.spi.Platform;

/**
 * @author Jean-Pierre Alonso.
 */
public class EventListenerImpl implements EventListener {
    private static final EventListener instance = new EventListenerImpl();   // singleton
    private int requested, started, connected;
    private MemData memData;

    public static EventListener getInstance() {
        return instance;
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getType()) {
            case REQUESTED:
                int val = event.getVal();
                if (val != requested)
                    requested=val;
                break;
            case CONNECTING:
                started ++;
                break;
            case CONNECTED:
                connected ++;
                break;
            case UNCONNECTING:
                started --;
                break;
            case NOT_CONNECTED:
                connected --;
                break;
            case SYSTEM:
                // TODO  onEvent SYSTEM : mettre à jour le SysBeanFX lu par MemeCharGenerator.
                memData = event.
            default:
                Platform.log("Event type unkwon !");
        }
    }

    @Override
    public int getRequested() {
        return requested;
    }

    @Override
    public int getStarted() {
        return started;
    }

    @Override
    public int getConnected() {
        return connected;
    }
}
