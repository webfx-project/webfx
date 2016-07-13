package mongoose.activities.monitor.listener;

/**
 * @author Jean-Pierre Alonso.
 */
public class EventBase implements Event {
    private long idEvent;
    private Object object;
    private EventType type;
    private int val;
    private long eventTime;

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public int getVal() {
        return val;
    }

    @Override
    public void setVal(int val) {
        this.val = val;
    }

    @Override
    public long getEventTime() {
        return eventTime;
    }

    @Override
    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public long getIdEvent() {
        return idEvent;
    }

    @Override
    public void setIdEvent(long idEvent) {
        this.idEvent = idEvent;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public void setObject(Object object) {
        this.object = object;
    }
}
