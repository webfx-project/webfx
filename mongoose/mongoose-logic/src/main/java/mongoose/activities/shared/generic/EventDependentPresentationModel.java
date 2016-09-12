package mongoose.activities.shared.generic;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.presentation.PresentationModel;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentPresentationModel implements PresentationModel, HasEventIdProperty {

    // Input parameter

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();
    public Property<Object> eventIdProperty() { return eventIdProperty; }

}
