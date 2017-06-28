package mongoose.entities;

import mongoose.entities.markers.EntityHasLabel;
import mongoose.entities.markers.EntityHasName;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Country extends Entity, EntityHasName, EntityHasLabel {

    default void setIsoAlpha2(String isoAlpha2) {
        setFieldValue("iso_alpha2", isoAlpha2);
    }

    default String getIsoAlpha2() {
        return getStringFieldValue("iso_alpha2");
    }

}
