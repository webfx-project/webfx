package mongooses.core.entities;

import mongooses.core.entities.markers.EntityHasLabel;
import mongooses.core.entities.markers.EntityHasName;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Organization extends Entity, EntityHasName, EntityHasLabel {
}
