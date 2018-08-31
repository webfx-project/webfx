package mongoose.entities;

import mongoose.entities.markers.EntityHasArrivalSiteAndItem;
import mongoose.entities.markers.EntityHasCancelled;
import mongoose.entities.markers.EntityHasDocument;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface DocumentLine extends Entity, EntityHasDocument, EntityHasCancelled, EntityHasArrivalSiteAndItem {

}
