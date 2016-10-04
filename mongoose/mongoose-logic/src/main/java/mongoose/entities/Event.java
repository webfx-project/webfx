package mongoose.entities;

import mongoose.entities.markers.*;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Event extends Entity, EntityHasName, EntityHasLabel, EntityHasDateTimeRange, EntityHasMinDateTimeRange, EntityHasMaxDateTimeRange, EntityHasOrganization {

}
