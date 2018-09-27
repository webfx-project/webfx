package mongooses.core.shared.entities;

import mongooses.core.shared.entities.markers.EntityHasEvent;
import mongooses.core.shared.entities.markers.EntityHasItemFamily;
import mongooses.core.shared.entities.markers.EntityHasLabel;
import mongooses.core.shared.entities.markers.EntityHasName;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Site extends Entity, EntityHasName, EntityHasLabel, EntityHasEvent, EntityHasItemFamily {

    //// Domain fields

    default void setMain(Boolean main) {
        setFieldValue("main", main);
    }

    default Boolean isMain() {
        return getBooleanFieldValue("main");
    }

}
