package mongoose.activities.event.shared;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.presentation.PresentationModel;

/**
 * @author Bruno Salmon
 */
public abstract class EventBasedPresentationModel implements PresentationModel {

    // Input parameter

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();
    public Property<Object> eventIdProperty() { return eventIdProperty; }

}
