package mongoose.backend.activities.loadtester.drive.model;

/**
 * @author Jean-Pierre Alonso.
 */
public interface ConnectionData {

    int getRequested();

    void setRequested(int requested);

    int getStarted();

    void setStarted(int started);

    int getConnected();

    void setConnected(int connected);

}
