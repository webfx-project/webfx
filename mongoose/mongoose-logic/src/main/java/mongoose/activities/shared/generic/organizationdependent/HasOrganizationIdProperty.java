package mongoose.activities.shared.generic.organizationdependent;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasOrganizationIdProperty {

    Property<Object> organizationIdProperty();

    default void setOrganizationId(Object organizationId) {
        this.organizationIdProperty().setValue(organizationId);
    }

    default Object getOrganizationId() {
        return this.organizationIdProperty().getValue();
    }

}
