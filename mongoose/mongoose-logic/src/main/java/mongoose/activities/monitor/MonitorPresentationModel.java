package mongoose.activities.monitor;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.presentation.PresentationModel;
import naga.toolkit.display.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
class MonitorPresentationModel implements PresentationModel {

//    private final Property<Integer> requestedConnectionsProperty = new SimpleObjectProperty<>(0);
//    Property<Integer> requestedConnectionsProperty() { return requestedConnectionsProperty; }
//
//    private final Property<Integer> startedConnectionsProperty = new SimpleObjectProperty<>(0);
//    Property<Integer> startedConnectionsProperty() { return startedConnectionsProperty; }

    private final Property<DisplayResultSet> chartDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> chartDisplayResultSetProperty() { return chartDisplayResultSetProperty; }

}
