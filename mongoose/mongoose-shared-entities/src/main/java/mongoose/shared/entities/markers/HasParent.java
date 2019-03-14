package mongoose.shared.entities.markers;

import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasParent<P extends Entity> {

    void setParent(Object parent);

    EntityId getParentId();

    P getParent();

}
