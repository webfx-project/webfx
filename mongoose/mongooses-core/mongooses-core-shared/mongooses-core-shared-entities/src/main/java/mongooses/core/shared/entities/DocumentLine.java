package mongooses.core.shared.entities;

import mongooses.core.shared.entities.markers.EntityHasArrivalSiteAndItem;
import mongooses.core.shared.entities.markers.EntityHasCancelled;
import mongooses.core.shared.entities.markers.EntityHasDocument;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface DocumentLine extends Entity, EntityHasDocument, EntityHasCancelled, EntityHasArrivalSiteAndItem {

}
