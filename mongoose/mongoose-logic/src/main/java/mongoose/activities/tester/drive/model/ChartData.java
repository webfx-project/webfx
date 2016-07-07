package mongoose.activities.tester.drive.model;

import javafx.beans.property.IntegerProperty;

/**
 * @author Jean-Pierre Alonso.
 */
public interface ChartData {
    int getRequested();
    IntegerProperty requestedProperty();
    void setRequested(int requested);
    int getStarted();
    IntegerProperty startedProperty();
    void setStarted(int started);
    int getConnected();
    IntegerProperty connectedProperty();
    void setConnected(int connected);
}
