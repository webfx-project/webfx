package mongooses.core.entities;

import mongooses.core.entities.markers.EntityHasArrivalSiteAndItem;
import mongooses.core.entities.markers.EntityHasCancelled;
import mongooses.core.entities.markers.EntityHasDocument;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface DocumentLine extends Entity, EntityHasDocument, EntityHasCancelled, EntityHasArrivalSiteAndItem {

}
