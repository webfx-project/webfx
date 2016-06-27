package mongoose.activities.monitor;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.presentation.PresentationModel;

/**
 * @author Bruno Salmon
 */
class MonitorPresentationModel implements PresentationModel {

    private final Property<Integer> requestedConnectionsProperty = new SimpleObjectProperty<>();
    Property<Integer> requestedConnectionsProperty() { return requestedConnectionsProperty; }

    private final Property<Integer> startedConnectionsProperty = new SimpleObjectProperty<>();
    Property<Integer> startedConnectionsProperty() { return startedConnectionsProperty; }

    // Display output

    private final Property<DisplayResultSet> chartDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> organizationsDisplayResultSetProperty() { return chartDisplayResultSetProperty; }

}
