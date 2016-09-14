package mongoose.activities.shared.generic;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Bruno Salmon
 */
public class GenericTableEventDependentPresentationModel extends GenericTablePresentationModel implements HasEventIdProperty {
    private final Property<Object> eventIdProperty = new SimpleObjectProperty();

    public GenericTableEventDependentPresentationModel() {
    }

    public Property<Object> eventIdProperty() {
        return this.eventIdProperty;
    }
}

