package mongooses.core.activities.backend.loadtester.drive.listener;

import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public class EventBase implements Event {
    private long idEvent;
    private Instant eventTime;
    private EventType type;
    private int val;
    private Object object;

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
    public Instant getEventTime() {
        return eventTime;
    }

    @Override
    public void setEventTime(Instant eventTime) {
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
