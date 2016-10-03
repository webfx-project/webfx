package mongoose.entities.markers;

import mongoose.entities.Site;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

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
