package mongoose.backend.activities.loadtester;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import webfx.fxkit.extra.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
final class LoadTesterPresentationModel {

    private final IntegerProperty requestedConnectionsProperty = new SimpleIntegerProperty(0);
    IntegerProperty requestedConnectionsProperty() { return requestedConnectionsProperty; }

    private final IntegerProperty startedConnectionsProperty = new SimpleIntegerProperty(0);
    IntegerProperty startedConnectionsProperty() { return startedConnectionsProperty; }

    private final Property<DisplayResult> chartDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> chartDisplayResultProperty() { return chartDisplayResultProperty; }

    private final ObjectProperty<EventHandler<ActionEvent>> onSaveTest = new SimpleObjectProperty<>();
    final ObjectProperty<EventHandler<ActionEvent>> onSaveTestProperty() { return onSaveTest; }
    final void setOnSaveTest(EventHandler<ActionEvent> value) { onSaveTestProperty().set(value); }
    final EventHandler<ActionEvent> getOnSaveTest() { return onSaveTestProperty().get(); }
}
