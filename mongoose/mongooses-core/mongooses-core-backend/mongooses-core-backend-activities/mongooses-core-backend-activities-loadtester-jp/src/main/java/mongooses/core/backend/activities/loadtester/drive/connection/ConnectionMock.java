package mongooses.core.backend.activities.loadtester.drive.connection;


import mongooses.core.backend.activities.loadtester.drive.listener.ConnectionEvent;
import mongooses.core.backend.activities.loadtester.drive.listener.EventType;
import mongooses.core.backend.activities.loadtester.drive.command.Command;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.scheduler.Scheduler;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionMock extends ConnectionBase {

    @Override
    public ConnectionEvent executeCommand(Command t) {
        ConnectionEvent event;
        switch (t) {
            case OPEN:
                event = new ConnectionEvent(EventType.CONNECTING);
                Scheduler.scheduleDelay(1000, SimConnection);
                break;
            case CLOSE:
                event = new ConnectionEvent(EventType.DISCONNECTING);
                Scheduler.scheduleDelay(500, SimClose);
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
        Logger.log("CnxMock-CONNECTED");
    };

    private Runnable SimClose = () -> {
        // Synchronous ?
        ConnectionEvent event = new ConnectionEvent(EventType.NOT_CONNECTED);
        applyEvent(event);
        recordEvent(event);
        Logger.log("CnxMock-CLOSED");
    };
}
