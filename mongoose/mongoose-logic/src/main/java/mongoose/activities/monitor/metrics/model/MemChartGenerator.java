package mongoose.activities.monitor.metrics.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.monitor.listener.EventListener;
import mongoose.activities.monitor.listener.EventListenerImpl;
import naga.commons.type.PrimType;
import naga.platform.spi.Platform;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplayResultSetBuilder;
import naga.toolkit.spi.Toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class MemChartGenerator {

    private List<MemData> memList = new ArrayList<>();
    private ObjectProperty<DisplayResultSet> memListProperty = new SimpleObjectProperty<>();

    public void start() {
        Platform.get().scheduler().schedulePeriodic(1000, this::readTask);
    }

    public DisplayResultSet createDisplayResultSet (){
//        Platform.log("createDisplayResultSet(System)");
        int rowCount = memList.size();
        // Building the DisplayResultSet for the chart using column format (First column = X, other columns = series Ys)
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(rowCount, new DisplayColumn[]{
                DisplayColumn.create("Time", PrimType.INTEGER),
                DisplayColumn.create("Total", PrimType.INTEGER),
                DisplayColumn.create("Free", PrimType.INTEGER),
                DisplayColumn.create("Free physical", PrimType.INTEGER)});
        for (int rowIndex=0 ; rowIndex<rowCount ; rowIndex++) {
            MemData data = memList.get(rowIndex);
            rsb.setValue(rowIndex, 0, rowIndex); // temporary taking rowIndex as X
            rsb.setValue(rowIndex, 1, data.totalMemProperty().getValue());
            rsb.setValue(rowIndex, 2, data.freeMemProperty().getValue());
            rsb.setValue(rowIndex, 3, data.freePhMemProperty().getValue());
        }
        DisplayResultSet displayResultSet = rsb.build();
//        Platform.log("Ok: " + displayResultSet);
        Platform.log("Chart - [" + rowCount
                    +", "+ memList.get(rowCount-1).totalMemProperty().getValue()
                    +", "+ memList.get(rowCount-1).freeMemProperty().getValue()
                    +", "+ memList.get(rowCount-1).freePhMemProperty().getValue()
                    +" ]");
        Toolkit.get().scheduler().scheduleDeferred(() -> memListProperty.set(displayResultSet));
        return displayResultSet;
    }

    private void readTask () {
        EventListener listener = EventListenerImpl.getInstance();
        MemData data = new MemData();

//        data.totalMemProperty().setValue(listener.getRequested());
//        data.startedProperty().setValue(listener.getStarted());
//        data.connectedProperty().setValue(listener.getConnected());
        // TODO initialise data with the last snapshot (binding ?)
        memList.add(data);
        createDisplayResultSet();
    }

    public DisplayResultSet getMemList() {
        return memListProperty.get();
    }

    public ObjectProperty<DisplayResultSet> memListProperty() {
        return memListProperty;
    }
}
