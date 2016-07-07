package mongoose.activities.tester.drive.model;

import javafx.beans.property.Property;

/**
 * @author Jean-Pierre Alonso.
 */
public interface ChartData {
    int getRequested();
    Property<Integer> requestedProperty();
    void setRequested(int requested);
    int getStarted();
    Property<Integer> startedProperty();
    void setStarted(int started);
    int getConnected();
    Property<Integer> connectedProperty();
    void setConnected(int connected);
}
