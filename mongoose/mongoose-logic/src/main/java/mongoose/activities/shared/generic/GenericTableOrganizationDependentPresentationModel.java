package mongoose.activities.shared.generic;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Bruno Salmon
 */
public class GenericTableOrganizationDependentPresentationModel extends GenericTablePresentationModel implements HasOrganizationIdProperty {

    private final Property<Object> organizationIdProperty = new SimpleObjectProperty<>();

    public Property<Object> organizationIdProperty() {
        return this.organizationIdProperty;
    }

}