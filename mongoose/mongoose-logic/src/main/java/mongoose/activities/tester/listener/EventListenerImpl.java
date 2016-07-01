package mongoose.activities.tester.listener;

/**
 * @author Jean-Pierre Alonso.
 */
public class EventListenerImpl implements EventListener {
    private static final EventListener instance = new EventListenerImpl();   // singleton
    private int requested, started, connected;
//    private SysBean sysBean;

    public static EventListener getInstance() {
        return instance;
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getType()) {
            case REQUESTED:
                int val = event.getVal();
                if (val != requested)
                    requested=val;
                break;
            case CONNECTING:
                started ++;
                break;
            case CONNECTED:
                connected ++;
                break;
            case UNCONNECTING:
                started --;
                break;
            case NOT_CONNECTED:
                connected --;
                break;
            default:
                System.err.println("Event type unkwon !");
        }
//        System.out.printf("Event %s / started : %d - connected : %d", event.getType(), started, connected).println();
    }

    @Override
    public int getRequested() {
        return requested;
    }

    @Override
    public int getStarted() {
        return started;
    }

    @Override
    public int getConnected() {
        return connected;
    }

}
