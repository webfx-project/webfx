package mongooses.core.shared.entities;

import mongooses.core.shared.entities.markers.EntityHasDate;
import mongooses.core.shared.entities.markers.EntityHasDocumentLine;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Attendance extends Entity, EntityHasDocumentLine, EntityHasDate {

}
