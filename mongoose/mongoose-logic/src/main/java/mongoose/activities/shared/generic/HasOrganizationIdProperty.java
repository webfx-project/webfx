package mongoose.activities.shared.generic;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasOrganizationIdProperty {

    Property<Object> organizationIdProperty();

    default void setOrganizationId(Object selected) {
        this.organizationIdProperty().setValue(selected);
    }

    default Object getOrganizationId() {
        return this.organizationIdProperty().getValue();
    }

}
