package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;

/**
 * @author Bruno Salmon
 */
public interface HasOrganizationIdProperty {

    ObjectProperty<Object> organizationIdProperty();

    default void setOrganizationId(Object organizationId) {
        this.organizationIdProperty().setValue(organizationId);
    }

    default Object getOrganizationId() {
        return this.organizationIdProperty().getValue();
    }

}
