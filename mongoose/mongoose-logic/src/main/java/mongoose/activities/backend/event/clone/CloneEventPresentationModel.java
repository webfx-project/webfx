package mongoose.activities.backend.event.clone;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationModel;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public class CloneEventPresentationModel extends EventDependentPresentationModel {

    private final Property<String> nameProperty = new SimpleObjectProperty<>();
    private final Property<LocalDate> dateProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onSubmit = new SimpleObjectProperty<>();

    public Property<String> nameProperty() {
        return nameProperty;
    }

    public void setName(String name) {
        nameProperty.setValue(name);
    }

    public String getName() {
        return nameProperty.getValue();
    }

    public Property<LocalDate> dateProperty() {
        return dateProperty;
    }

    public void setDate(LocalDate date) {
        dateProperty.setValue(date);
    }

    public LocalDate getDate() {
        return dateProperty.getValue();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onSubmitProperty() { return onSubmit; }
    public final void setOnSubmit(EventHandler<ActionEvent> value) { onSubmitProperty().set(value); }
    public final EventHandler<ActionEvent> getOnSubmit() { return onSubmitProperty().get(); }

}
