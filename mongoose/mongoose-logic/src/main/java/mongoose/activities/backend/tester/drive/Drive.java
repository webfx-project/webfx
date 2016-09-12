package mongoose.activities.backend.tester.drive;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.backend.monitor.listener.ConnectionEvent;
import mongoose.activities.backend.monitor.listener.Event;
import mongoose.activities.backend.monitor.listener.EventListenerImpl;
import mongoose.activities.backend.monitor.listener.EventType;
import mongoose.activities.backend.tester.drive.command.Command;
import mongoose.activities.backend.tester.drive.connection.Connection;
import mongoose.activities.backend.tester.drive.connection.WebSocketBusConnection;
import mongoose.entities.LtTestEvent;
import mongoose.entities.LtTestEventEntity;
import mongoose.entities.LtTestSet;
import mongoose.entities.LtTestSetEntity;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.UpdateStore;
import naga.platform.bus.call.BusCallService;
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
    private int nextToRemove = 0;

    public static Drive getInstance() {
        return instance;
    }

    public void start(boolean mode_console) {
        long t0 = System.currentTimeMillis();
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
                    started++;
                } else {
                    // We must close existing connections
                    Connection cnx = connexionList.get(nextToRemove);
                    cnx.executeCommand(Command.CLOSE);
                    nextToRemove++;
                    started--;
                }
                Toolkit.get().scheduler().scheduleDeferred(() -> startedConnectionCount.setValue(started));

                if (mode_console)
                    Platform.log("Drive - connections : R="+ requested
                            +" , S="+ started
                            +" (time = "+ (System.currentTimeMillis()-t0) +"ms)");
            }
        });
    }
    public void recordTestSet(DataSourceModel dataSourceModel, String testName, String testComment) {
        UpdateStore store = UpdateStore.create(dataSourceModel);
        LtTestSet testSet = store.insertEntity(LtTestSetEntity.class);
        testSet.setName(testName);
        testSet.setComment(testComment);
        // All events of each connection are read by the getUncommitedEvent method
        for (Connection connection : connexionList) {
            List<ConnectionEvent> eventList = connection.getUncommitedEvents();
            for (Event e : eventList) {
                LtTestEvent event = store.insertEntity(LtTestEventEntity.class);
                event.setLtTestSet(testSet);
                event.setEventTime(e.getEventTime());
                event.setType(e.getType());
                event.setVal(e.getVal());
            }
        }
        // Writing the result in the database
        store.executeUpdate().setHandler(asyncResult -> {
            if (asyncResult.failed())
                Platform.log(asyncResult.cause());
            else {
                connexionList.clear();
                nextToRemove = 0;
                Platform.log("Recorded !!! :-)");
            }
        });

    }
}
