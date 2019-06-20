package mongoose.client.activity.eventdependent;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.client.activity.table.GenericTablePresentationModel;

/**
 * @author Bruno Salmon
 */
public class EventDependentGenericTablePresentationModel extends GenericTablePresentationModel
        implements EventDependentPresentationModel {

    private final ObjectProperty<Object> eventIdProperty = new SimpleObjectProperty<>();

    public ObjectProperty<Object> eventIdProperty() {
        return this.eventIdProperty;
    }

    private final ObjectProperty<Object> organizationIdProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<Object> organizationIdProperty() {
        return organizationIdProperty;
    }
}

