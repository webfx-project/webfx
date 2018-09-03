package mongooses.core.entities;

import mongooses.core.entities.markers.EntityHasLabel;
import mongooses.core.entities.markers.EntityHasName;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Method extends Entity,
        EntityHasName,
        EntityHasLabel {

    int ONLINE_METHOD_ID = 5; // hardcoded id that matches the database (not beautiful)

}
