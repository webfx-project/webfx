package mongoose.activities.backend.monitor.metrics.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.backend.monitor.metrics.Metrics;
import naga.platform.services.log.spi.Logger;
import naga.scheduler.Scheduler;
import naga.type.PrimType;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplayResultSetBuilder;
import naga.fx.spi.Toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class MemChartGenerator {

    private List<MemData> memList = new ArrayList<>();
    private ObjectProperty<DisplayResultSet> memListProperty = new SimpleObjectProperty<>();

    public void start() {
        Scheduler.schedulePeriodic(1000, this::readTask);
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
        Logger.log("Chart - [" + rowCount
                    +", "+ memList.get(rowCount-1).totalMemProperty().getValue()
                    +", "+ memList.get(rowCount-1).freeMemProperty().getValue()
                    +", "+ memList.get(rowCount-1).freePhMemProperty().getValue()
                    +" ]");
        Toolkit.get().scheduler().scheduleDeferred(() -> memListProperty.set(displayResultSet));
        return displayResultSet;
    }

    private void readTask () {
//        System.out.println("readTask() called in " + Thread.currentThread().getName());
        memList.add(Metrics.getInstance().getMemData());
        createDisplayResultSet();
    }

    public DisplayResultSet getMemList() {
        return memListProperty.get();
    }

    public ObjectProperty<DisplayResultSet> memListProperty() {
        return memListProperty;
    }
}
