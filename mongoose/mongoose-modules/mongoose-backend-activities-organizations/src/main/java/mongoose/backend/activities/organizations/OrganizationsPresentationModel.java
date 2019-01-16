package mongoose.backend.activities.organizations;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.client.activity.table.GenericTablePresentationModel;

/**
 * @author Bruno Salmon
 */
final class OrganizationsPresentationModel extends GenericTablePresentationModel {

    // Display input

    private final Property<Boolean> withEventsProperty = new SimpleObjectProperty<>(true); // Initially set to true
    Property<Boolean> withEventsProperty() { return withEventsProperty; }

}
