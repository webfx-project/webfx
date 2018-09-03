package mongooses.core.activities.backend.loadtester.drive.listener;


import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionEvent extends EventBase {

    public ConnectionEvent(EventType type) {
        setType(type);
        setEventTime(Instant.now());
        setVal(0);
    }

    public ConnectionEvent(EventType type, int requested) {
        setType(type);
        setEventTime(Instant.now());
        setVal(requested);
    }
}
