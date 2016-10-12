package mongoose.entities;

import mongoose.entities.markers.EntityHasCancelled;
import mongoose.entities.markers.EntityHasEvent;
import mongoose.entities.markers.EntityHasPerson;
import mongoose.entities.markers.EntityHasPersonDetailsCopy;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Document extends Entity, EntityHasEvent, EntityHasCancelled, EntityHasPerson, EntityHasPersonDetailsCopy {

    default void setRef(Integer ref) {
        setFieldValue("ref", ref);
    }

    default Integer getRef() {
        return getIntegerFieldValue("ref");
    }

}
