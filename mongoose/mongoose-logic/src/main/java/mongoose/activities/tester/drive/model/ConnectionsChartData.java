package mongoose.activities.tester.drive.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionsChartData implements ChartData {
    private final IntegerProperty requested = new SimpleIntegerProperty();
    private final IntegerProperty started = new SimpleIntegerProperty();
    private final IntegerProperty connected = new SimpleIntegerProperty();

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

    @Override
    public void setRequested(int requested) {
        this.requested.set(requested);
    }

    @Override
    public void setStarted(int started) {
        this.started.set(started);
    }

    @Override
    public void setConnected(int connected) {
        this.connected.set(connected);
    }
}
