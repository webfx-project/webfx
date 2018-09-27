package mongoose.shared.entities.markers;

import mongoose.shared.entities.Site;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface EntityHasSite extends Entity, HasSite {

    @Override
    default void setSite(Object site) {
        setForeignField("site", site);
    }

    @Override
    default EntityId getSiteId() {
        return getForeignEntityId("site");
    }

    @Override
    default Site getSite() {
        return getForeignEntity("site");
    }


}
