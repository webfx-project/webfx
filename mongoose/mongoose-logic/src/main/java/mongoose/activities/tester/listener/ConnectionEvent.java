package mongoose.activities.tester.listener;


import mongoose.activities.tester.listener.EventBase;
import mongoose.activities.tester.listener.EventType;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionEvent extends EventBase {

    public ConnectionEvent(EventType type) {
        setType(type);
        setEventTime(System.currentTimeMillis());
        setVal(0);
    }

    public ConnectionEvent(EventType type, int requested) {
        setType(type);
        setEventTime(System.currentTimeMillis());
        setVal(requested);
    }
}
