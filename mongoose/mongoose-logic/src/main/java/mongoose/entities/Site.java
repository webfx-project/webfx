package mongoose.entities;

import mongoose.entities.markers.EntityHasEvent;
import mongoose.entities.markers.EntityHasItemFamily;
import mongoose.entities.markers.EntityHasLabel;
import mongoose.entities.markers.EntityHasName;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Site extends Entity, EntityHasName, EntityHasLabel, EntityHasEvent, EntityHasItemFamily {
}
