package mongoose.activities.tester.drive.connection;

import mongoose.activities.tester.drive.command.Command;
import mongoose.activities.tester.listener.ConnectionEvent;
import mongoose.activities.tester.listener.EventType;
import naga.platform.spi.Platform;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionImpl extends ConnectionBase {

    @Override
    public ConnectionEvent executeCommand(Command t) {
//        Scheduler scheduler = Platform.get().scheduler();
        ConnectionEvent event;
        switch (t) {
            case OPEN:
                event = new ConnectionEvent(EventType.CONNECTING);
                bus = Platform.createBus();
//                scheduler.scheduleDelay(1000, SimConnection);
                break;
            case CLOSE:
                event = new ConnectionEvent(EventType.UNCONNECTING);
                bus.close();
//                scheduler.scheduleDelay(500, SimClose);
                break;
            default:
                event = null;
        }
        applyEvent(event);
        recordEvent(event);
        return event;
    }
}
