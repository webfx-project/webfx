package mongoose.entities;

import mongoose.entities.markers.EntityHasDate;
import mongoose.entities.markers.EntityHasDocumentLine;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Attendance extends Entity, EntityHasDocumentLine, EntityHasDate {

}
