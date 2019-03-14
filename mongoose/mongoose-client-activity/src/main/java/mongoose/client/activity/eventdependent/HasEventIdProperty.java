package mongoose.client.activity.eventdependent;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasEventIdProperty {

    Property<Object> eventIdProperty();

    default void setEventId(Object eventId) {
        eventIdProperty().setValue(eventId);
    }

    default Object getEventId() {
        return eventIdProperty().getValue();
    }
}
