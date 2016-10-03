package mongoose.entities;

import mongoose.entities.markers.EntityHasLabel;
import mongoose.entities.markers.EntityHasName;
import mongoose.entities.markers.EntityHasOrganization;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Event extends Entity, EntityHasName, EntityHasLabel, EntityHasOrganization {

}
