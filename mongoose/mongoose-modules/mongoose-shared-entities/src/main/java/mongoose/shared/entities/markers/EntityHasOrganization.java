package mongoose.shared.entities.markers;

import mongoose.shared.entities.Organization;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface EntityHasOrganization extends Entity, HasOrganization {

    @Override
    default void setOrganization(Object organization) {
        setForeignField("organization", organization);
    }

    @Override
    default EntityId getOrganizationId() {
        return getForeignEntityId("organization");
    }

    @Override
    default Organization getOrganization() {
        return getForeignEntity("organization");
    }


}
