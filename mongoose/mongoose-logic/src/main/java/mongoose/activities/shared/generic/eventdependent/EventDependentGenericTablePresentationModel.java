package mongoose.activities.shared.generic.eventdependent;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.generic.table.GenericTablePresentationModel;

/**
 * @author Bruno Salmon
 */
public class EventDependentGenericTablePresentationModel extends GenericTablePresentationModel
        implements HasEventIdProperty {

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();

    public Property<Object> eventIdProperty() {
        return this.eventIdProperty;
    }
}

