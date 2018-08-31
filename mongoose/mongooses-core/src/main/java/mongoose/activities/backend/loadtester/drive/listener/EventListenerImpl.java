package mongoose.activities.backend.loadtester.drive.listener;

import mongoose.activities.backend.loadtester.drive.metrics.model.MemData;
import webfx.platform.services.log.Logger;

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
            case DISCONNECTING:
                started --;
                break;
            case NOT_CONNECTED:
                connected --;
                break;
            case SYSTEM:
                memData = (MemData)event.getObject();
            default:
                Logger.log("Event type unkwon !");
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

    @Override
    public MemData getMemData() {
        return memData;
    }
}
