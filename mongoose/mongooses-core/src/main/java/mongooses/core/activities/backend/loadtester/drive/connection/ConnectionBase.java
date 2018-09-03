package mongooses.core.activities.backend.loadtester.drive.connection;


import mongooses.core.activities.backend.loadtester.drive.listener.ConnectionEvent;
import mongooses.core.activities.backend.loadtester.drive.listener.EventListener;
import mongooses.core.activities.backend.loadtester.drive.listener.EventListenerImpl;
import webfx.platforms.core.services.log.Logger;

import java.util.ArrayList;
import java.util.List;

import static mongooses.core.activities.backend.loadtester.drive.connection.ConnectionState.*;

/**
 * @author Jean-Pierre Alonso.
 */
public abstract class ConnectionBase implements Connection {
    int id;
    private ConnectionState state = NOT_CONNECTED;
    EventListener listener = EventListenerImpl.getInstance();
    protected List<ConnectionEvent> uncommitedEventList = new ArrayList<>();    // List of events on this specific connection

    public ConnectionBase () {
        super();
    }

    // Parse all kind of ConnectionEvent
    @Override
    public void applyEvent(ConnectionEvent event) {
        switch (event.getType()) {
            case CONNECTING:
                state = CONNECTING;
                break;
            case CONNECTED:
                state = CONNECTED;
                break;
            case DISCONNECTING:
                state = UNCONNECTING;
                break;
            case NOT_CONNECTED:
                state = NOT_CONNECTED;
                break;
            default:
                state = null;
                Logger.log("Connection state undefined !");
        }
        if (listener != null) {
            listener.onEvent(event);
//            Platform.log("CnxApply "+event.getType()+" ");
        }
    }

    protected void recordEvent(ConnectionEvent event) {
        uncommitedEventList.add(event);
    }

    @Override
    public List<ConnectionEvent> getUncommitedEvents() {
        return uncommitedEventList;
    }

    @Override
    public ConnectionState getState() {
        return state;
    }
}
