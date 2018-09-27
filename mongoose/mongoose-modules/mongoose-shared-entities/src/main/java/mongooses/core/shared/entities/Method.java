package mongooses.core.shared.entities;

import mongooses.core.shared.entities.markers.EntityHasLabel;
import mongooses.core.shared.entities.markers.EntityHasName;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Method extends Entity,
        EntityHasName,
        EntityHasLabel {

    int ONLINE_METHOD_ID = 5; // hardcoded id that matches the database (not beautiful)

}
