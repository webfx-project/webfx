package mongoose.activities.backend.monitor;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.presentation.PresentationModel;
import naga.toolkit.display.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
class MonitorPresentationModel implements PresentationModel {

    private final Property<DisplayResultSet> memoryDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> memoryDisplayResultSetProperty() { return memoryDisplayResultSetProperty; }

    private final Property<DisplayResultSet> cpuDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> cpuDisplayResultSetProperty() { return cpuDisplayResultSetProperty; }
}
