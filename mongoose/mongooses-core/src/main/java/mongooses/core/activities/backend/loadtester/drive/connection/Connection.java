package mongooses.core.activities.backend.loadtester.drive.connection;


import mongooses.core.activities.backend.loadtester.drive.command.Command;
import mongooses.core.activities.backend.loadtester.drive.listener.ConnectionEvent;

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
