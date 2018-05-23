package mongoose.activities.backend.loadtester.drive.model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionsChartData implements ConnectionData {
    private final Property<Integer> requested = new SimpleObjectProperty<>(0);
    private final Property<Integer> started = new SimpleObjectProperty(0);
    private final Property<Integer> connected = new SimpleObjectProperty(0);

    @Override
    public int getRequested() {
        return requested.getValue();
    }

    @Override
    public Property<Integer> requestedProperty() {
        return requested;
    }

    @Override
    public int getStarted() {
        return started.getValue();
    }

    @Override
    public Property<Integer> startedProperty() {
        return started;
    }

    @Override
    public int getConnected() {
        return connected.getValue();
    }

    @Override
    public Property<Integer> connectedProperty() {
        return connected;
    }

    @Override
    public void setRequested(int requested) {
        this.requested.setValue(requested);
    }

    @Override
    public void setStarted(int started) {
        this.started.setValue(started);
    }

    @Override
    public void setConnected(int connected) {
        this.connected.setValue(connected);
    }
}
