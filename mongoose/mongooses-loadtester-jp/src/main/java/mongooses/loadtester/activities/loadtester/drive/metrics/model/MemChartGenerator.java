package mongooses.loadtester.activities.loadtester.drive.metrics.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongooses.loadtester.activities.loadtester.drive.metrics.Metrics;
import webfx.fxkits.extra.displaydata.DisplayResult;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.scheduler.Scheduler;
import webfx.fxkits.extra.type.PrimType;
import webfx.fxkits.extra.displaydata.DisplayColumn;
import webfx.fxkits.extra.displaydata.DisplayResultBuilder;
import webfx.fxkits.core.spi.FxKit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class MemChartGenerator {

    private List<MemData> memList = new ArrayList<>();
    private ObjectProperty<DisplayResult> memListProperty = new SimpleObjectProperty<>();

    public void start() {
        Scheduler.schedulePeriodic(1000, this::readTask);
    }

    public DisplayResult createDisplayResult(){
//        Platform.log("createDisplayResult(System)");
        int rowCount = memList.size();
        // Building the DisplayResult for the chart using column format (First column = X, other columns = series Ys)
        DisplayResultBuilder rsb = DisplayResultBuilder.create(rowCount, new DisplayColumn[]{
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
        DisplayResult displayResult = rsb.build();
//        Platform.log("Ok: " + displayResult);
        Logger.log("Chart - [" + rowCount
                    +", "+ memList.get(rowCount-1).totalMemProperty().getValue()
                    +", "+ memList.get(rowCount-1).freeMemProperty().getValue()
                    +", "+ memList.get(rowCount-1).freePhMemProperty().getValue()
                    +" ]");
        FxKit.get().scheduler().scheduleDeferred(() -> memListProperty.set(displayResult));
        return displayResult;
    }

    private void readTask () {
//        System.out.println("readTask() called in " + Thread.currentThread().getName());
        memList.add(Metrics.getInstance().getMemData());
        createDisplayResult();
    }

    public DisplayResult getMemList() {
        return memListProperty.get();
    }

    public ObjectProperty<DisplayResult> memListProperty() {
        return memListProperty;
    }
}
