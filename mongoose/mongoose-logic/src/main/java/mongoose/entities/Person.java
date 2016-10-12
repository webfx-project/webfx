package mongoose.entities;

import mongoose.entities.markers.EntityHasOrganization;
import mongoose.entities.markers.EntityHasPersonDetails;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Person extends Entity, EntityHasOrganization, EntityHasPersonDetails {

}
