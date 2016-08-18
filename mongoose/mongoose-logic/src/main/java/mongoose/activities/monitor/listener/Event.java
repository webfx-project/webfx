package mongoose.activities.monitor.listener;

import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public interface Event {
    EventType getType();

    void setType(EventType type);

    int getVal();

    void setVal(int val);

    Instant getEventTime();

    void setEventTime(Instant eventTime);

    long getIdEvent();

    void setIdEvent(long idEvent);

    Object getObject();

    void setObject(Object object);
}
