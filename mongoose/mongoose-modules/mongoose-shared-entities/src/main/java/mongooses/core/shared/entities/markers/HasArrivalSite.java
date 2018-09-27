package mongooses.core.shared.entities.markers;

import mongooses.core.shared.entities.Site;
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
