package mongoose.backend.activities.cloneevent;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mongoose.client.activity.eventdependent.EventDependentPresentationModelImpl;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public final class CloneEventPresentationModel extends EventDependentPresentationModelImpl {

    private final Property<String> nameProperty = new SimpleObjectProperty<>();
    private final Property<LocalDate> dateProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onSubmit = new SimpleObjectProperty<>();

    Property<String> nameProperty() {
        return nameProperty;
    }

    void setName(String name) {
        nameProperty.setValue(name);
    }

    String getName() {
        return nameProperty.getValue();
    }

    public Property<LocalDate> dateProperty() {
        return dateProperty;
    }

    void setDate(LocalDate date) {
        dateProperty.setValue(date);
    }

    LocalDate getDate() {
        return dateProperty.getValue();
    }

    final ObjectProperty<EventHandler<ActionEvent>> onSubmitProperty() { return onSubmit; }
    final void setOnSubmit(EventHandler<ActionEvent> value) { onSubmitProperty().set(value); }
    final EventHandler<ActionEvent> getOnSubmit() { return onSubmitProperty().get(); }

}
