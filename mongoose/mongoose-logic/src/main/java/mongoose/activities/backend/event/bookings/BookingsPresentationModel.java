package mongoose.activities.backend.event.bookings;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mongoose.activities.shared.generic.GenericTableEventDependentPresentationModel;

/**
 * @author Bruno Salmon
 */
public class BookingsPresentationModel extends GenericTableEventDependentPresentationModel {

    private final ObjectProperty<EventHandler<ActionEvent>> onNewBooking = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onNewBookingProperty() { return onNewBooking; }
    public final void setOnNewBooking(EventHandler<ActionEvent> value) { onNewBookingProperty().set(value); }
    public final EventHandler<ActionEvent> getOnNewBooking() { return onNewBookingProperty().get(); }

    private final ObjectProperty<EventHandler<ActionEvent>> onCloneEvent = new SimpleObjectProperty<>();
    public final ObjectProperty<EventHandler<ActionEvent>> onCloneEventProperty() { return onCloneEvent; }
    public final void setOnCloneEvent(EventHandler<ActionEvent> value) { onCloneEventProperty().set(value); }
    public final EventHandler<ActionEvent> getOnCloneEvent() { return onCloneEventProperty().get(); }

}
