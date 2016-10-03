package mongoose.entities.markers;

import mongoose.entities.Site;
import naga.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasSite {

    void setSite(Object site);

    EntityId getSiteId();

    Site getSite();

}
