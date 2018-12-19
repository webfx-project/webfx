package mongoose.backend.activities.loadtester.drive.connection;

import mongoose.backend.activities.loadtester.drive.command.Command;
import mongoose.backend.activities.loadtester.drive.listener.ConnectionEvent;
import mongoose.backend.activities.loadtester.drive.listener.EventType;
import webfx.platform.client.services.websocket.WebSocketListener;
import webfx.platform.client.services.websocketbus.WebSocketBus;
import webfx.platform.shared.services.bus.BusService;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.log.Logger;

/**
 * @author Jean-Pierre Alonso.
 */
public class WebSocketBusConnection extends ConnectionBase {
    protected WebSocketBus bus;
    boolean opened;

    @Override
    public ConnectionEvent executeCommand(Command t) {
        ConnectionEvent event;
        switch (t) {
            case OPEN:
                bus = (WebSocketBus) BusService.createBus();
                bus.setWebSocketListener(createWebSocketListener());
                event = new ConnectionEvent(EventType.CONNECTING);
                break;
            case CLOSE:
                event = new ConnectionEvent(EventType.DISCONNECTING);
                bus.close();
                break;
            default:
                event = null;
        }
        applyEvent(event);
        recordEvent(event);
        return event;
    }

    private WebSocketListener createWebSocketListener() {
        return new WebSocketListener() {
            @Override
            public void onOpen() {
                opened = true;
                ConnectionEvent event1 = new ConnectionEvent(EventType.CONNECTED);
                applyEvent(event1);
                recordEvent(event1);
                Logger.log("Cnx-CONNECTED : "+bus.toString());
            }

            @Override
            public void onClose(JsonObject reason) {
                if (opened) {
                    ConnectionEvent event1 = new ConnectionEvent(EventType.NOT_CONNECTED);
                    applyEvent(event1);
                    recordEvent(event1);
                    opened = false;
                }
                Logger.log("Cnx-CLOSED : "+reason);
            }

            @Override
            public void onMessage(String message) {
//                Logger.log("Cnx-MESSAGE : "+message);
            }

            @Override
            public void onError(String error) {
                Logger.log("Cnx-ERROR ("+bus.toString()+") : "+error);
            }
        };
    }
}
