package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasArrivalSiteAndItem;
import mongoose.shared.entities.markers.EntityHasCancelled;
import mongoose.shared.entities.markers.EntityHasDocument;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface DocumentLine extends Entity, EntityHasDocument, EntityHasCancelled, EntityHasArrivalSiteAndItem {

}
