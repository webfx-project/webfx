package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;

/**
 * @author Bruno Salmon
 */
public interface HasEventIdProperty {

    ObjectProperty<Object> eventIdProperty();

    default void setEventId(Object eventId) {
        eventIdProperty().setValue(eventId);
    }

    default Object getEventId() {
        return eventIdProperty().getValue();
    }
}
