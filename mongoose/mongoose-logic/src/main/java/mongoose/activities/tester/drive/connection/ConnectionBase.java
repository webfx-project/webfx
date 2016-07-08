package mongoose.activities.tester.drive.connection;


import mongoose.activities.tester.listener.ConnectionEvent;
import mongoose.activities.tester.listener.EventListener;
import mongoose.activities.tester.listener.EventListenerImpl;
import mongoose.activities.tester.listener.EventType;
import naga.commons.bus.call.BusCallService;
import naga.commons.bus.websocket.WebSocketBus;
import naga.commons.json.spi.JsonObject;
import naga.commons.websocket.spi.WebSocketListener;
import naga.platform.spi.Platform;

import java.util.ArrayList;
import java.util.List;

import static mongoose.activities.tester.drive.connection.ConnectionState.*;

/**
 * @author Jean-Pierre Alonso.
 */
public abstract class ConnectionBase implements Connection {
    int id;
    private ConnectionState state = NOT_CONNECTED;
    EventListener listener = EventListenerImpl.getInstance();
    protected List<ConnectionEvent> uncommitedEventList = new ArrayList<>();    // List of events on this specific connection
    protected WebSocketBus bus;
    protected WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen() {
            ConnectionEvent event = new ConnectionEvent(EventType.CONNECTED);
            applyEvent(event);
            recordEvent(event);
            Platform.log("Cnx-CONNECTED");
        }

        @Override
        public void onClose(JsonObject reason) {
            ConnectionEvent event = new ConnectionEvent(EventType.NOT_CONNECTED);
            applyEvent(event);
            recordEvent(event);
            Platform.log("Cnx-CLOSED");
        }

        @Override
        public void onMessage(String message) {
            Platform.log("Cnx-MESSAGE : "+message);
        }

        @Override
        public void onError(String error) {
            Platform.log("Cnx-ERROR : "+error);
        }
    };

    public ConnectionBase () {
        super();
        BusCallService.call("version", "ignored").setHandler(asyncResult -> Platform.log(asyncResult.succeeded() ? asyncResult.result() : "Error: " + asyncResult.cause()));
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
            case UNCONNECTING:
                state = UNCONNECTING;
                break;
            case NOT_CONNECTED:
                state = NOT_CONNECTED;
                break;
            default:
                state = null;
                Platform.log("Connection state undefined !");
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
