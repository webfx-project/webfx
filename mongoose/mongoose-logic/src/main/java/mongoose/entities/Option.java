package mongoose.entities;

import mongoose.entities.markers.EntityHasItem;
import mongoose.entities.markers.EntityHasLabel;
import mongoose.entities.markers.EntityHasName;
import mongoose.entities.markers.EntityHasSite;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Option extends Entity, EntityHasName, EntityHasLabel, EntityHasSite, EntityHasItem {
}
