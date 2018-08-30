package mongoose.entities.markers;

import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface EntityHasName extends Entity, HasName {

    @Override
    default void setName(String name) {
        setFieldValue("name", name);
    }

    @Override
    default String getName() {
        return getStringFieldValue("name");
    }
}
