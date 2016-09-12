package mongoose.activities.organizations;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.GenericTablePresentationModel;

/**
 * @author Bruno Salmon
 */
class OrganizationsPresentationModel extends GenericTablePresentationModel {

    // Display input

    private final Property<Boolean> withEventsProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    Property<Boolean> withEventsProperty() { return withEventsProperty; }

}
