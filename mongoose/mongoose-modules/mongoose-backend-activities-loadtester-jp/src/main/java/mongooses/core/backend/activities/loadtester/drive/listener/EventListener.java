package mongooses.core.backend.activities.loadtester.drive.listener;

import mongooses.core.backend.activities.loadtester.drive.metrics.model.MemData;

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
