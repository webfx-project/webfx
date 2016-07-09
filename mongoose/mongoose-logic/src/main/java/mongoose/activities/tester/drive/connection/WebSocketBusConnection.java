package mongoose.activities.tester.drive.connection;

import mongoose.activities.tester.drive.command.Command;
import mongoose.activities.tester.listener.ConnectionEvent;
import mongoose.activities.tester.listener.EventType;
import naga.commons.bus.call.BusCallService;
import naga.commons.bus.websocket.WebSocketBus;
import naga.commons.json.spi.JsonObject;
import naga.commons.websocket.spi.WebSocketListener;
import naga.platform.spi.Platform;

/**
 * @author Jean-Pierre Alonso.
 */
public class WebSocketBusConnection extends ConnectionBase {
    protected WebSocketBus bus;

    @Override
    public ConnectionEvent executeCommand(Command t) {
//        Scheduler scheduler = Platform.get().scheduler();
        ConnectionEvent event;
        switch (t) {
            case OPEN:
                event = new ConnectionEvent(EventType.CONNECTING);
                bus = (WebSocketBus)Platform.createBus();
                bus.setWebSocketListener(createWebSocketListener());
                long t0 = System.currentTimeMillis();
                BusCallService.call("version", "ignored", bus).setHandler(asyncResult -> {
                    long t1 = System.currentTimeMillis();
                    Platform.log((asyncResult.succeeded() ? asyncResult.result() : "Error: " + asyncResult.cause()) + " in " + (t1 - t0) + "ms");
                });
                break;
            case CLOSE:
                event = new ConnectionEvent(EventType.UNCONNECTING);
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
                ConnectionEvent event1 = new ConnectionEvent(EventType.CONNECTED);
                applyEvent(event1);
                recordEvent(event1);
                Platform.log("Cnx-CONNECTED");
            }

            @Override
            public void onClose(JsonObject reason) {
                ConnectionEvent event1 = new ConnectionEvent(EventType.NOT_CONNECTED);
                applyEvent(event1);
                recordEvent(event1);
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
    }
}
