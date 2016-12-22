package mongoose.activities.backend.event.clone;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.generic.EventDependentPresentationModel;

/**
 * @author Bruno Salmon
 */
class CloneEventPresentationModel extends EventDependentPresentationModel {

    private final Property<String> nameProperty = new SimpleObjectProperty<>();
    private final Property<String> dateProperty = new SimpleObjectProperty<>();

    public Property<String> nameProperty() {
        return nameProperty;
    }

    public void setName(String name) {
        nameProperty.setValue(name);
    }

    public String getName() {
        return nameProperty.getValue();
    }

    public Property<String> dateProperty() {
        return dateProperty;
    }

    public void setDate(String date) {
        dateProperty.setValue(date);
    }

    public String getDate() {
        return dateProperty.getValue();
    }

}
