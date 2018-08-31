package mongoose.entities;

import mongoose.entities.markers.EntityHasLabel;
import mongoose.entities.markers.EntityHasName;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Organization extends Entity, EntityHasName, EntityHasLabel {
}
