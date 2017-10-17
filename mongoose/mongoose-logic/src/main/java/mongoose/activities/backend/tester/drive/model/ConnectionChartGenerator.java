package mongoose.activities.backend.tester.drive.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.backend.monitor.listener.EventListener;
import mongoose.activities.backend.monitor.listener.EventListenerImpl;
import naga.platform.spi.Platform;
import naga.fx.spi.Toolkit;
import naga.type.PrimType;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplayResultSetBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionChartGenerator {

    private List<ConnectionsChartData> connectionList = new ArrayList<>();
    private ObjectProperty<DisplayResultSet> connectionListProperty = new SimpleObjectProperty<>();

    public void start() {
        Platform.get().scheduler().schedulePeriodic(1000, this::readTask);
    }

    public void reset() {
        connectionList.clear();
    }

    public DisplayResultSet createDisplayResultSet (){
//        Platform.log("createDisplayResultSet(Connections)");
        int rowCount = connectionList.size();
        // Building the DisplayResultSet for the chart using column format (First column = X, other columns = series Ys)
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(rowCount, new DisplayColumn[]{
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
        DisplayResultSet displayResultSet = rsb.build();
//        Platform.log("Ok: " + displayResultSet);
/*
        Platform.log("Chart - [" + rowCount
                    +", "+ connectionList.get(rowCount-1).getRequested()
                    +", "+ connectionList.get(rowCount-1).getStarted()
                    +", "+ connectionList.get(rowCount-1).getConnected()
                    +" ]");
*/
        Toolkit.get().scheduler().scheduleDeferred(() -> connectionListProperty.set(displayResultSet));
        return displayResultSet;
    }

    private void readTask () {
        EventListener listener = EventListenerImpl.getInstance();
        ConnectionsChartData data = new ConnectionsChartData();

        data.requestedProperty().setValue(listener.getRequested());
        data.startedProperty().setValue(listener.getStarted());
        data.connectedProperty().setValue(listener.getConnected());
        connectionList.add(data);
        createDisplayResultSet();
    }

    public DisplayResultSet getConnectionList() {
        return connectionListProperty.get();
    }

    public ObjectProperty<DisplayResultSet> connectionListProperty() {
        return connectionListProperty;
    }
}
