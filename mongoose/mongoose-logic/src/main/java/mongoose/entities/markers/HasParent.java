package mongoose.entities.markers;

import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasParent<P extends Entity> {

    void setParent(Object parent);

    EntityId getParentId();

    P getParent();

}
