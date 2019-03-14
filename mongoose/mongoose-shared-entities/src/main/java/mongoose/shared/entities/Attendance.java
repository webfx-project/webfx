package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasDate;
import mongoose.shared.entities.markers.EntityHasDocumentLine;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Attendance extends Entity, EntityHasDocumentLine, EntityHasDate {

}
