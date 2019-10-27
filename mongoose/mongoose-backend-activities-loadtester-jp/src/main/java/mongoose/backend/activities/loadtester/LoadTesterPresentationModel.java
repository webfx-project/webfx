package mongoose.backend.activities.loadtester;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import webfx.extras.visual.VisualResult;

/**
 * @author Bruno Salmon
 */
final class LoadTesterPresentationModel {

    private final IntegerProperty requestedConnectionsProperty = new SimpleIntegerProperty(0);
    IntegerProperty requestedConnectionsProperty() { return requestedConnectionsProperty; }

    private final IntegerProperty startedConnectionsProperty = new SimpleIntegerProperty(0);
    IntegerProperty startedConnectionsProperty() { return startedConnectionsProperty; }

    private final Property<VisualResult> chartVisualResultProperty = new SimpleObjectProperty<>();
    Property<VisualResult> chartVisualResultProperty() { return chartVisualResultProperty; }

    private final ObjectProperty<EventHandler<ActionEvent>> onSaveTest = new SimpleObjectProperty<>();
    final ObjectProperty<EventHandler<ActionEvent>> onSaveTestProperty() { return onSaveTest; }
    final void setOnSaveTest(EventHandler<ActionEvent> value) { onSaveTestProperty().set(value); }
    final EventHandler<ActionEvent> getOnSaveTest() { return onSaveTestProperty().get(); }
}
