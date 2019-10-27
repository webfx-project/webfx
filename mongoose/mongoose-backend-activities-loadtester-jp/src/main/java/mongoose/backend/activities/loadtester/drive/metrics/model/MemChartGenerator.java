package mongoose.backend.activities.loadtester.drive.metrics.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.backend.activities.loadtester.drive.metrics.Metrics;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualResultBuilder;
import webfx.extras.type.PrimType;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class MemChartGenerator {

    private List<MemData> memList = new ArrayList<>();
    private ObjectProperty<VisualResult> memListProperty = new SimpleObjectProperty<>();

    public void start() {
        Scheduler.schedulePeriodic(1000, this::readTask);
    }

    public VisualResult createVisualResult(){
//        Logger.log("createVisualResult(System)");
        int rowCount = memList.size();
        // Building the VisualResult for the chart using column format (First column = X, other columns = series Ys)
        VisualResultBuilder rsb = VisualResultBuilder.create(rowCount, new VisualColumn[]{
                VisualColumn.create("Time", PrimType.INTEGER),
                VisualColumn.create("Total", PrimType.INTEGER),
                VisualColumn.create("Free", PrimType.INTEGER),
                VisualColumn.create("Free physical", PrimType.INTEGER)});
        for (int rowIndex=0 ; rowIndex<rowCount ; rowIndex++) {
            MemData data = memList.get(rowIndex);
            rsb.setValue(rowIndex, 0, rowIndex); // temporary taking rowIndex as X
            rsb.setValue(rowIndex, 1, data.totalMem());
            rsb.setValue(rowIndex, 2, data.freeMem());
            rsb.setValue(rowIndex, 3, data.freePhMem());
        }
        VisualResult visualResult = rsb.build();
//        Logger.log("Ok: " + visualResult);
        /*
        Logger.log("Chart - [" + rowCount
                    +", "+ memList.get(rowCount-1).totalMem()
                    +", "+ memList.get(rowCount-1).freeMem()
                    +", "+ memList.get(rowCount-1).freePhMem()
                    +" ]");
        */
        UiScheduler.runInUiThread(() -> memListProperty.set(visualResult));
        return visualResult;
    }

    private void readTask () {
//        System.out.println("readTask() called in " + Thread.currentThread().getName());
        memList.add(Metrics.getInstance().getMemData());
        createVisualResult();
    }

    public VisualResult getMemList() {
        return memListProperty.get();
    }

    public ObjectProperty<VisualResult> memListProperty() {
        return memListProperty;
    }
}
