package mongooses.core.sharedends.activities.generic.organizationdependent;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongooses.core.sharedends.activities.generic.table.GenericTablePresentationModel;

/**
 * @author Bruno Salmon
 */
public class OrganizationDependentGenericTablePresentationModel
        extends GenericTablePresentationModel
        implements HasOrganizationIdProperty {

    private final Property<Object> organizationIdProperty = new SimpleObjectProperty<>();

    public Property<Object> organizationIdProperty() {
        return organizationIdProperty;
    }

}