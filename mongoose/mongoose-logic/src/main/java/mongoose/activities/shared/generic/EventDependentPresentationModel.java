package mongoose.activities.shared.generic;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentPresentationModel implements HasEventIdProperty {

    // Input parameter

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();
    public Property<Object> eventIdProperty() { return eventIdProperty; }

}
