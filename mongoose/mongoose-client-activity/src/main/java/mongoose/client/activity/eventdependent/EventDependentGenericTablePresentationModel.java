package mongoose.client.activity.eventdependent;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.client.activity.table.GenericTablePresentationModel;

/**
 * @author Bruno Salmon
 */
public class EventDependentGenericTablePresentationModel extends GenericTablePresentationModel
        implements EventDependentPresentationModel {

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();

    public Property<Object> eventIdProperty() {
        return this.eventIdProperty;
    }

    private final Property<Object> organizationIdProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Object> organizationIdProperty() {
        return organizationIdProperty;
    }
}

