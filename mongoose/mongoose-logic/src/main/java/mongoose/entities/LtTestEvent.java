package mongoose.entities;

import mongoose.activities.monitor.listener.EventType;

/**
 * @author Jean-Pierre Alonso.
 */
public interface LtTestEvent {

    Long getEventTime();

    void setEventTime(Long eventTime);

    EventType getType();

    void setType(EventType type);

    Integer getVal();

    void setVal(Integer val);

    long getEventId();
}
