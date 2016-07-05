package mongoose.activities.tester.drive.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionsChartData implements ChartData {
    private IntegerProperty requested = new SimpleIntegerProperty();
    private IntegerProperty started = new SimpleIntegerProperty();
    private IntegerProperty connected = new SimpleIntegerProperty();

    @Override
    public int getRequested() {
        return requested.getValue();
    }

    @Override
    public IntegerProperty requestedProperty() {
        return requested;
    }

    @Override
    public int getStarted() {
        return started.get();
    }

    @Override
    public IntegerProperty startedProperty() {
        return started;
    }

    @Override
    public int getConnected() {
        return connected.get();
    }

    @Override
    public IntegerProperty connectedProperty() {
        return connected;
    }
}
