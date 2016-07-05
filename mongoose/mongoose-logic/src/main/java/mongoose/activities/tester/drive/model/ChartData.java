package mongoose.activities.tester.drive.model;

import javafx.beans.property.IntegerProperty;

/**
 * @author Jean-Pierre Alonso.
 */
public interface ChartData {
    int getRequested();
    IntegerProperty requestedProperty();
    int getStarted();
    IntegerProperty startedProperty();
    int getConnected();
    IntegerProperty connectedProperty();
}
