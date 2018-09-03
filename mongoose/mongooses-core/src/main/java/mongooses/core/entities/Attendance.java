package mongooses.core.entities;

import mongooses.core.entities.markers.EntityHasDate;
import mongooses.core.entities.markers.EntityHasDocumentLine;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Attendance extends Entity, EntityHasDocumentLine, EntityHasDate {

}
