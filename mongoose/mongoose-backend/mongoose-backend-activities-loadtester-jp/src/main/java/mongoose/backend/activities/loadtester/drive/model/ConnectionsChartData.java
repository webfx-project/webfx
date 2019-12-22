package mongoose.backend.activities.loadtester.drive.model;

/**
 * @author Jean-Pierre Alonso.
 */
public class ConnectionsChartData implements ConnectionData {
    private int requested;
    private int started;
    private int connected;

    @Override
    public int getRequested() {
        return requested;
    }

    @Override
    public int getStarted() {
        return started;
    }

    @Override
    public int getConnected() {
        return connected;
    }

    @Override
    public void setRequested(int requested) {
        this.requested = requested;
    }

    @Override
    public void setStarted(int started) {
        this.started = started;
    }

    @Override
    public void setConnected(int connected) {
        this.connected = connected;
    }
}
