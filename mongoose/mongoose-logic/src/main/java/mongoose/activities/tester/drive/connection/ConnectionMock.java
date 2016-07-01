package mongoose.activities.tester.drive.connection;


import mongoose.activities.tester.drive.command.Command;
import mongoose.activities.tester.listener.ConnectionEvent;
import mongoose.activities.tester.listener.EventType;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionMock extends ConnectionBase {

    @Override
    public ConnectionEvent executeCommand(Command t) {
        Scheduler scheduler = Platform.get().scheduler();
        ConnectionEvent event;
        switch (t) {
            case OPEN:
                event = new ConnectionEvent(EventType.CONNECTING);
                scheduler.scheduleDelay(1000, SimConnection);
                break;
            case CLOSE:
                event = new ConnectionEvent(EventType.UNCONNECTING);
                scheduler.scheduleDelay(500, SimClose);
                break;
            default:
                event = null;
        }
        applyEvent(event);
        recordEvent(event);
        return event;
    }

    private Runnable SimConnection = () -> {
        // Synchronous ?
        ConnectionEvent event = new ConnectionEvent(EventType.CONNECTED);
        applyEvent(event);
        recordEvent(event);
        System.out.println("CnxMock-CONNECTED");
    };

    private Runnable SimClose = () -> {
        // Synchronous ?
        ConnectionEvent event = new ConnectionEvent(EventType.NOT_CONNECTED);
        applyEvent(event);
        recordEvent(event);
        System.out.println("CnxMock-CLOSED");
    };
}
