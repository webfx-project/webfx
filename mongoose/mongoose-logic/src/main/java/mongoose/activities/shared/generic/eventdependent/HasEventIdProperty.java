package mongoose.activities.shared.generic.eventdependent;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasEventIdProperty {

    Property<Object> eventIdProperty();

    default void setEventId(Object selected) {
        this.eventIdProperty().setValue(selected);
    }

    default Object getEventId() {
        return this.eventIdProperty().getValue();
    }
}
