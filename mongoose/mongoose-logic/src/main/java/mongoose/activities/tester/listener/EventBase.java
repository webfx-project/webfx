package mongoose.activities.tester.listener;

/**
 * @author Jean-Pierre Alonso.
 */
public class EventBase implements Event {
    private long idEvent;
    private long idObject;
    private EventType type;
    private long eventTime;
    private int val;

    @Override
    public EventType getType() {
        return type;
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
    public void setType(EventType type) {
        this.type = type;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(long idEvent) {
        this.idEvent = idEvent;
    }
}
