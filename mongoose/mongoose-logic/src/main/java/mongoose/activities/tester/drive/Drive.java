package mongoose.activities.tester.drive;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.tester.drive.command.Command;
import mongoose.activities.tester.drive.connection.Connection;
import mongoose.activities.tester.drive.connection.WebSocketBusConnection;
import mongoose.activities.tester.listener.ConnectionEvent;
import mongoose.activities.tester.listener.EventListenerImpl;
import mongoose.activities.tester.listener.EventType;
import naga.commons.bus.call.BusCallService;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class Drive {
    private static final Drive instance = new Drive();   // singleton

    private final Property<Integer> requestedConnectionCount = new SimpleObjectProperty<>(0);
    public  Property<Integer> requestedConnectionCountProperty() {return requestedConnectionCount;}
    private final Property<Integer> startedConnectionCount = new SimpleObjectProperty<>(0);
    public  Property<Integer> startedConnectionCountProperty() {return startedConnectionCount;}
    private final List<Connection> connexionList = new ArrayList<>();
    private int currentRequested = 0;
    private int started = 0;

    boolean mode_console;

    public static Drive getInstance() {
        return instance;
    }

    public void start(boolean mode_console) {
        this.mode_console = mode_console;
        BusCallService.call("version", "ignored").setHandler(asyncResult -> Platform.log(asyncResult.succeeded() ? asyncResult.result() : "Error: " + asyncResult.cause()));
        Platform.get().scheduler().schedulePeriodic(10, () -> {
            int requested = requestedConnectionCount.getValue();

            if (currentRequested != requested) {
                EventListenerImpl.getInstance().onEvent(new ConnectionEvent(EventType.REQUESTED, requested));
                currentRequested = requested;
            }
            if (started != requested) {
                if (started < requested) {
                    // We must start new connections
                    Connection cnx = new WebSocketBusConnection();
                    connexionList.add(cnx);
                    cnx.executeCommand(Command.OPEN);
                    ++started;
                } else {
                    // We must close existing connections
                    Connection cnx = connexionList.get(0);
                    cnx.executeCommand(Command.CLOSE);
                    connexionList.remove(0);
                    --started;
                }
                Toolkit.get().scheduler().scheduleDeferred(() -> startedConnectionCount.setValue(started));

                if (mode_console)
                    Platform.log("Drive - connections : "+ requested
                            +" , "+ started
                            +" (time = "+ System.currentTimeMillis() +")");
            }
        });
    }
}
