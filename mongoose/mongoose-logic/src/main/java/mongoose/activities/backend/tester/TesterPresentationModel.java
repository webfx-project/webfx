package mongoose.activities.backend.tester;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import naga.fxdata.displaydata.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
public class TesterPresentationModel {

    private final Property<Integer> requestedConnectionsProperty = new SimpleObjectProperty<>(0);
    Property<Integer> requestedConnectionsProperty() { return requestedConnectionsProperty; }

    private final Property<Integer> startedConnectionsProperty = new SimpleObjectProperty<>(0);
    Property<Integer> startedConnectionsProperty() { return startedConnectionsProperty; }

    private final Property<DisplayResultSet> chartDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> chartDisplayResultSetProperty() { return chartDisplayResultSetProperty; }

    private final ObjectProperty<EventHandler<ActionEvent>> onSaveTest = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onSaveTestProperty() { return onSaveTest; }
    public final void setOnSaveTest(EventHandler<ActionEvent> value) { onSaveTestProperty().set(value); }
    public final EventHandler<ActionEvent> getOnSaveTest() { return onSaveTestProperty().get(); }
}
