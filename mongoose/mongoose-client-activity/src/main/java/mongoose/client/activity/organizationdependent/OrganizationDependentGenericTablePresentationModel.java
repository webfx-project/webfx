package mongoose.client.activity.organizationdependent;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.client.activity.table.GenericTablePresentationModel;
import mongoose.client.presentationmodel.HasOrganizationIdProperty;

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