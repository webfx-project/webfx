package mongoose.entities;

import mongoose.entities.markers.EntityHasItem;
import mongoose.entities.markers.EntityHasSite;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Rate extends Entity, EntityHasSite, EntityHasItem {
}
