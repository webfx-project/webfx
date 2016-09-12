package mongoose.activities.backend.events;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.generic.GenericTableOrganizationDependentPresentationModel;

/**
 * @author Bruno Salmon
 */
class EventsPresentationModel extends GenericTableOrganizationDependentPresentationModel {

    // Display input

    private final Property<Boolean> withBookingsProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    Property<Boolean> withBookingsProperty() { return withBookingsProperty; }

}
