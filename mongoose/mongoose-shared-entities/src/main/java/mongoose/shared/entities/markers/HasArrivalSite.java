package mongoose.shared.entities.markers;

import mongoose.shared.entities.Site;
import webfx.framework.shared.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasArrivalSite {

    void setArrivalSite(Object site);

    EntityId getArrivalSiteId();

    Site getArrivalSite();

    default boolean hasArrivalSite() {
        return getArrivalSite() != null;
    }

}
