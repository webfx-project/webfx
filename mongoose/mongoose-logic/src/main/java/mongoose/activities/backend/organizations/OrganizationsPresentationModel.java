package mongoose.activities.backend.organizations;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.generic.table.GenericTablePresentationModel;

/**
 * @author Bruno Salmon
 */
public class OrganizationsPresentationModel extends GenericTablePresentationModel {

    // Display input

    private final Property<Boolean> withEventsProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    Property<Boolean> withEventsProperty() { return withEventsProperty; }

}
