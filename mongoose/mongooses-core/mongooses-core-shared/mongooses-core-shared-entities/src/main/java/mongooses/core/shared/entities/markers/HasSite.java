package mongooses.core.shared.entities.markers;

import mongooses.core.shared.entities.Site;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasSite {

    void setSite(Object site);

    EntityId getSiteId();

    Site getSite();

    default boolean hasSite() {
        return getSite() != null;
    }

}
