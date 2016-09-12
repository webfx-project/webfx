package mongoose.entities;

import mongoose.activities.backend.monitor.listener.EventType;

import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public interface LtTestEvent {

    void setLtTestSet(Object ltTestSet);

    LtTestSet getLtTestSet();

    Instant getEventTime();

    void setEventTime(Instant eventTime);

    EventType getType();

    void setType(EventType type);

    Integer getVal();

    void setVal(Integer val);

//    long getEventId();
}
