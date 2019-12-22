package mongoose.backend.activities.loadtester.drive.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.backend.activities.loadtester.drive.listener.EventListener;
import mongoose.backend.activities.loadtester.drive.listener.EventListenerImpl;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualResultBuilder;
import webfx.extras.type.PrimType;
import webfx.platform.shared.services.scheduler.Scheduler;
import webfx.platform.client.services.uischeduler.UiScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionChartGenerator {

    private List<ConnectionsChartData> connectionList = new ArrayList<>();
    private ObjectProperty<VisualResult> connectionListProperty = new SimpleObjectProperty<>();

    public void start() {
        Scheduler.schedulePeriodic(1000, this::readTask);
    }

    public void reset() {
        connectionList.clear();
    }

    public VisualResult createVisualResult(){
//        Logger.log("createVisualResult(Connections)");
        int rowCount = connectionList.size();
        // Building the VisualResult for the chart using column format (First column = X, other columns = series Ys)
        VisualResultBuilder rsb = VisualResultBuilder.create(rowCount, new VisualColumn[]{
                VisualColumn.create("Time", PrimType.INTEGER),
                VisualColumn.create("Requested", PrimType.INTEGER),
                VisualColumn.create("Started", PrimType.INTEGER),
                VisualColumn.create("Connected", PrimType.INTEGER)});
        for (int rowIndex=0 ; rowIndex<rowCount ; rowIndex++) {
            ConnectionsChartData data = connectionList.get(rowIndex);
            rsb.setValue(rowIndex, 0, rowIndex); // TODO temporary taking rowIndex as X, should take event_time
            rsb.setValue(rowIndex, 1, data.getRequested());
            rsb.setValue(rowIndex, 2, data.getStarted());
            rsb.setValue(rowIndex, 3, data.getConnected());
        }
        VisualResult visualResult = rsb.build();
//        Logger.log("Ok: " + visualResult);
/*
        Logger.log("Chart - [" + rowCount
                    +", "+ connectionList.get(rowCount-1).getRequested()
                    +", "+ connectionList.get(rowCount-1).getStarted()
                    +", "+ connectionList.get(rowCount-1).getConnected()
                    +" ]");
*/
        UiScheduler.runInUiThread(() -> connectionListProperty.set(visualResult));
        return visualResult;
    }

    private void readTask () {
        EventListener listener = EventListenerImpl.getInstance();
        ConnectionsChartData data = new ConnectionsChartData();

        data.setRequested(listener.getRequested());
        data.setStarted(listener.getStarted());
        data.setConnected(listener.getConnected());
        connectionList.add(data);
        createVisualResult();
    }

    public VisualResult getConnectionList() {
        return connectionListProperty.get();
    }

    public ObjectProperty<VisualResult> connectionListProperty() {
        return connectionListProperty;
    }
}
