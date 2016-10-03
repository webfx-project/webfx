package mongoose.entities;

import mongoose.entities.markers.EntityHasDocument;
import mongoose.entities.markers.EntityHasItem;
import mongoose.entities.markers.EntityHasSite;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface DocumentLine extends Entity, EntityHasDocument, EntityHasSite, EntityHasItem {

}
