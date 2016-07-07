package mongoose.activities.tester.drive.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.tester.listener.EventListener;
import mongoose.activities.tester.listener.EventListenerImpl;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.Toolkit;
import naga.core.type.PrimType;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayresultset.DisplayResultSetBuilder;

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

    public DisplayResultSet createDisplayResultSet (){
//        Platform.log("createDisplayResultSet(Connections)");
        int rowCount = connectionList.size();
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(rowCount, new DisplayColumn[]{
                DisplayColumn.create("Requested", PrimType.INTEGER),
                DisplayColumn.create("Started", PrimType.INTEGER),
                DisplayColumn.create("Connected", PrimType.INTEGER)});
        for (int rowIndex=0 ; rowIndex<rowCount ; rowIndex++) {
            ConnectionsChartData data = connectionList.get(rowIndex);
            rsb.setValue(rowIndex, 0, data.getRequested());
            rsb.setValue(rowIndex, 1, data.getStarted());
            rsb.setValue(rowIndex, 2, data.getConnected());
        }
        DisplayResultSet displayResultSet = rsb.build();
        Platform.log("Ok: " + displayResultSet);
        Toolkit.get().scheduler().scheduleDeferred(() -> connectionListProperty.set(displayResultSet));
        return displayResultSet;
    }

    private void readTask () {
        EventListener listener = EventListenerImpl.getInstance();
        ConnectionsChartData data = new ConnectionsChartData();

        data.requestedProperty().set(listener.getRequested());
        data.startedProperty().set(listener.getStarted());
        data.connectedProperty().set(listener.getConnected());
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
