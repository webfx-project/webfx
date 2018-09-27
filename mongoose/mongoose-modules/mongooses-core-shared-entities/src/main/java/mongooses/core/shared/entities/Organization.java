package mongooses.core.shared.entities;

import mongooses.core.shared.entities.markers.EntityHasLabel;
import mongooses.core.shared.entities.markers.EntityHasName;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Organization extends Entity, EntityHasName, EntityHasLabel {
}
