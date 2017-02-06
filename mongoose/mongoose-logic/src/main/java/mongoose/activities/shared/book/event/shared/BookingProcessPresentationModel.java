package mongoose.activities.shared.book.event.shared;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationModel;

/**
 * @author Bruno Salmon
 */
public class BookingProcessPresentationModel implements EventDependentPresentationModel {

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();
    public Property<Object> eventIdProperty() { return eventIdProperty; }

    private final ObjectProperty<EventHandler<ActionEvent>> onPreviousAction = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onPreviousActionProperty() { return onPreviousAction; }
    public final void setOnPreviousAction(EventHandler<ActionEvent> value) { onPreviousActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnPreviousAction() { return onPreviousActionProperty().get(); }

    private final ObjectProperty<EventHandler<ActionEvent>> onNextAction = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onNextActionProperty() { return onNextAction; }
    public final void setOnNextAction(EventHandler<ActionEvent> value) { onNextActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnNextAction() { return onNextActionProperty().get(); }

}
