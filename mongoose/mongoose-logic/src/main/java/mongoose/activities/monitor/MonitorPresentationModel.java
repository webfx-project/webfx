package mongoose.activities.monitor;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.presentation.PresentationModel;
import naga.toolkit.display.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
class MonitorPresentationModel implements PresentationModel {

    private final Property<DisplayResultSet> memChartDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> memChartDisplayResultSetProperty() { return memChartDisplayResultSetProperty; }

    private final Property<DisplayResultSet> sysChartDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> sysChartDisplayResultSetProperty() { return sysChartDisplayResultSetProperty; }
}
