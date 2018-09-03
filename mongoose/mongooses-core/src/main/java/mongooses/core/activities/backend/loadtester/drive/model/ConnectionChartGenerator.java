package mongooses.core.activities.backend.loadtester.drive.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongooses.core.activities.backend.loadtester.drive.listener.EventListener;
import mongooses.core.activities.backend.loadtester.drive.listener.EventListenerImpl;
import webfx.fxkits.core.spi.FxKit;
import webfx.fxkits.extra.displaydata.DisplayColumn;
import webfx.fxkits.extra.displaydata.DisplayResult;
import webfx.fxkits.extra.displaydata.DisplayResultBuilder;
import webfx.platforms.core.services.scheduler.Scheduler;
import webfx.fxkits.extra.type.PrimType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionChartGenerator {

    private List<ConnectionsChartData> connectionList = new ArrayList<>();
    private ObjectProperty<DisplayResult> connectionListProperty = new SimpleObjectProperty<>();

    public void start() {
        Scheduler.schedulePeriodic(1000, this::readTask);
    }

    public void reset() {
        connectionList.clear();
    }

    public DisplayResult createDisplayResult(){
//        Platform.log("createDisplayResult(Connections)");
        int rowCount = connectionList.size();
        // Building the DisplayResult for the chart using column format (First column = X, other columns = series Ys)
        DisplayResultBuilder rsb = DisplayResultBuilder.create(rowCount, new DisplayColumn[]{
                DisplayColumn.create("Time", PrimType.INTEGER),
                DisplayColumn.create("Requested", PrimType.INTEGER),
                DisplayColumn.create("Started", PrimType.INTEGER),
                DisplayColumn.create("Connected", PrimType.INTEGER)});
        for (int rowIndex=0 ; rowIndex<rowCount ; rowIndex++) {
            ConnectionsChartData data = connectionList.get(rowIndex);
            rsb.setValue(rowIndex, 0, rowIndex); // TODO temporary taking rowIndex as X, should take event_time
            rsb.setValue(rowIndex, 1, data.getRequested());
            rsb.setValue(rowIndex, 2, data.getStarted());
            rsb.setValue(rowIndex, 3, data.getConnected());
        }
        DisplayResult displayResult = rsb.build();
//        Platform.log("Ok: " + displayResult);
/*
        Platform.log("Chart - [" + rowCount
                    +", "+ connectionList.get(rowCount-1).getRequested()
                    +", "+ connectionList.get(rowCount-1).getStarted()
                    +", "+ connectionList.get(rowCount-1).getConnected()
                    +" ]");
*/
        FxKit.get().scheduler().scheduleDeferred(() -> connectionListProperty.set(displayResult));
        return displayResult;
    }

    private void readTask () {
        EventListener listener = EventListenerImpl.getInstance();
        ConnectionsChartData data = new ConnectionsChartData();

        data.requestedProperty().setValue(listener.getRequested());
        data.startedProperty().setValue(listener.getStarted());
        data.connectedProperty().setValue(listener.getConnected());
        connectionList.add(data);
        createDisplayResult();
    }

    public DisplayResult getConnectionList() {
        return connectionListProperty.get();
    }

    public ObjectProperty<DisplayResult> connectionListProperty() {
        return connectionListProperty;
    }
}
