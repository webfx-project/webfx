package mongoose.activities.monitor.listener;

/**
 * @author Jean-Pierre Alonso.
 */
public interface Event {
    EventType getType();

    void setType(EventType type);

    int getVal();

    void setVal(int val);

    long getEventTime();

    void setEventTime(long eventTime);

    long getIdEvent();

    void setIdEvent(long idEvent);

    Object getObject();

    void setObject(Object object);
}
