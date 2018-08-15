package mongoose.activities.backend.organizations;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.bothends.generic.table.GenericTablePresentationModel;

/**
 * @author Bruno Salmon
 */
class OrganizationsPresentationModel extends GenericTablePresentationModel {

    // Display input

    private final Property<Boolean> withEventsProperty = new SimpleObjectProperty<>(true); // Initially set to true
    Property<Boolean> withEventsProperty() { return withEventsProperty; }

}
