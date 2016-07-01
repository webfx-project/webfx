package mongoose.activities.tester.listener;

/**
 * @author Jean-Pierre Alonso.
 */
public interface Event {
    EventType getType();
    void setType(EventType type);
    int getVal();
    void setVal(int val);
    long getEventTime();
}
