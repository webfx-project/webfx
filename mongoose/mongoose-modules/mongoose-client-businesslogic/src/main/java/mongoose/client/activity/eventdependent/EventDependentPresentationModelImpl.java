package mongoose.client.activity.eventdependent;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Bruno Salmon
 */
public class EventDependentPresentationModelImpl implements EventDependentPresentationModel {

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();
    public Property<Object> eventIdProperty() { return eventIdProperty; }

    private final Property<Object> organizationIdProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Object> organizationIdProperty() {
        return organizationIdProperty;
    }
}
