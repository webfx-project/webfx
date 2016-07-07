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
        DisplayColumn[] columns = new DisplayColumn[]{
                new DisplayColumn("Requested", PrimType.INTEGER),
                new DisplayColumn("Started", PrimType.INTEGER),
                new DisplayColumn("Connected", PrimType.INTEGER)};
        int columnCount = columns.length;
        Object[] values = new Object[rowCount * columnCount];
        int index = 0;
        for (int i=0 ; i<rowCount ; i++) {
            Integer value = connectionList.get(i).getRequested();
            values[index++] = value;
            value = connectionList.get(i).getStarted();
            values[index++] = value;
            value = connectionList.get(i).getConnected();
            values[index++] = value;
        }
        DisplayResultSet displayResultSet = new DisplayResultSet(rowCount, values, columns);
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
