package mongoose.client.activity.bookingprocess;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mongoose.client.activity.eventdependent.EventDependentPresentationModelImpl;

/**
 * @author Bruno Salmon
 */
public class BookingProcessPresentationModel extends EventDependentPresentationModelImpl {

    private final ObjectProperty<EventHandler<ActionEvent>> onPreviousAction = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onPreviousActionProperty() { return onPreviousAction; }
    public final void setOnPreviousAction(EventHandler<ActionEvent> value) { onPreviousActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnPreviousAction() { return onPreviousActionProperty().get(); }

    private final ObjectProperty<EventHandler<ActionEvent>> onNextAction = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onNextActionProperty() { return onNextAction; }
    public final void setOnNextAction(EventHandler<ActionEvent> value) { onNextActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnNextAction() { return onNextActionProperty().get(); }

}
