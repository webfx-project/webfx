package mongoose.client.activity.eventdependent;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Bruno Salmon
 */
public class EventDependentPresentationModelImpl implements EventDependentPresentationModel {

    private final ObjectProperty<Object> eventIdProperty = new SimpleObjectProperty<>();
    public ObjectProperty<Object> eventIdProperty() { return eventIdProperty; }

    private final ObjectProperty<Object> organizationIdProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<Object> organizationIdProperty() {
        return organizationIdProperty;
    }
}
