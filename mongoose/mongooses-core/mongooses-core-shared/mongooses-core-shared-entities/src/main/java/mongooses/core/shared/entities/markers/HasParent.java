package mongooses.core.shared.entities.markers;

import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasParent<P extends Entity> {

    void setParent(Object parent);

    EntityId getParentId();

    P getParent();

}
