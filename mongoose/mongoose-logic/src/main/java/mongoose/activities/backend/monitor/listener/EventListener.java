package mongoose.activities.backend.monitor.listener;

import mongoose.activities.backend.monitor.metrics.model.MemData;

/**
 * @author Jean-Pierre Alonso.
 */
public interface EventListener {

    void onEvent(Event event);
    int getRequested();
    int getStarted();
    int getConnected();
    MemData getMemData();
}
