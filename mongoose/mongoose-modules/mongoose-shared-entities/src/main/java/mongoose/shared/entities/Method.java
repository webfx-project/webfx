package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasLabel;
import mongoose.shared.entities.markers.EntityHasName;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Method extends Entity,
        EntityHasName,
        EntityHasLabel {

    int ONLINE_METHOD_ID = 5; // hardcoded id that matches the database (not beautiful)

}
