package mongoose.activities.tester;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.display.DisplayResultSet;
import naga.framework.ui.presentation.PresentationModel;

/**
 * @author Bruno Salmon
 */
class TesterPresentationModel implements PresentationModel {

    private final Property<Integer> requestedConnectionsProperty = new SimpleObjectProperty<>(0);
    Property<Integer> requestedConnectionsProperty() { return requestedConnectionsProperty; }

    private final Property<Integer> startedConnectionsProperty = new SimpleObjectProperty<>(0);
    Property<Integer> startedConnectionsProperty() { return startedConnectionsProperty; }

    private final Property<DisplayResultSet> chartDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> chartDisplayResultSetProperty() { return chartDisplayResultSetProperty; }

}
