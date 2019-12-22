package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasEvent;
import mongoose.shared.entities.markers.EntityHasItemFamily;
import mongoose.shared.entities.markers.EntityHasLabel;
import mongoose.shared.entities.markers.EntityHasName;
import webfx.framework.shared.orm.entity.Entity;

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

    default String getIcon() { return (String) evaluate("icon"); }

}
