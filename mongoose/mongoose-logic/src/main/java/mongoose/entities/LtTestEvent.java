package mongoose.entities;

import mongoose.activities.monitor.listener.EventType;

/**
 * @author Jean-Pierre Alonso.
 */
public interface LtTestEvent {

    void setLtTestSet(Object ltTestSet);

    LtTestSet getLtTestSet();

    Long getEventTime();

    void setEventTime(Long eventTime);

    EventType getType();

    void setType(EventType type);

    Integer getVal();

    void setVal(Integer val);

//    long getEventId();
}
