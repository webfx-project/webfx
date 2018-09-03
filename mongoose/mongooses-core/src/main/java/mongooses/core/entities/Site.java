package mongooses.core.entities;

import mongooses.core.entities.markers.EntityHasEvent;
import mongooses.core.entities.markers.EntityHasItemFamily;
import mongooses.core.entities.markers.EntityHasLabel;
import mongooses.core.entities.markers.EntityHasName;
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
