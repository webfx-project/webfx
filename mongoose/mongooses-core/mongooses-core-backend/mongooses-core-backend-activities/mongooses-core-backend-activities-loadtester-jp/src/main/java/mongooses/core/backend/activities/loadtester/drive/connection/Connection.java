package mongooses.core.backend.activities.loadtester.drive.connection;


import mongooses.core.backend.activities.loadtester.drive.command.Command;
import mongooses.core.backend.activities.loadtester.drive.listener.ConnectionEvent;

import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public interface Connection {
    List<ConnectionEvent> getUncommitedEvents();
    ConnectionState getState();
    ConnectionEvent executeCommand(Command cmd);
    void applyEvent(ConnectionEvent evt);
//    void setEventListener(EventListener listener);
}
