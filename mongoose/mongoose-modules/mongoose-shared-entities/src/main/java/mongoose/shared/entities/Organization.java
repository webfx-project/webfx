package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasLabel;
import mongoose.shared.entities.markers.EntityHasName;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Organization extends Entity, EntityHasName, EntityHasLabel {
}
