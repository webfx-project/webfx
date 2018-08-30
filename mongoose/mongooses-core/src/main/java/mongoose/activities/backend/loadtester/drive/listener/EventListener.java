package mongoose.activities.backend.loadtester.drive.listener;

import mongoose.activities.backend.loadtester.drive.metrics.model.MemData;

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
