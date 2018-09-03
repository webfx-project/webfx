package mongooses.core.entities.markers;

import mongooses.core.entities.Site;
import webfx.framework.orm.entity.EntityId;

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
