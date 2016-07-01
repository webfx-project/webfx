package mongoose.activities.tester;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.tester.drive.connection.Connection;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.presentation.PresentationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
class TesterPresentationModel implements PresentationModel {

    private final Property<Integer> requestedConnectionsProperty = new SimpleObjectProperty<>(0);
    Property<Integer> requestedConnectionsProperty() { return requestedConnectionsProperty; }

    private final Property<Integer> startedConnectionsProperty = new SimpleObjectProperty<>(0);
    Property<Integer> startedConnectionsProperty() { return startedConnectionsProperty; }

    private final List<Connection> connexionList = new ArrayList<>();

    // Display output

    private final Property<DisplayResultSet> chartDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> organizationsDisplayResultSetProperty() { return chartDisplayResultSetProperty; }

}
