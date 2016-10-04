package mongoose.entities.markers;

import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface EntityHasCode extends Entity, HasCode {

    @Override
    default void setCode(String code) {
        setFieldValue("code", code);
    }

    @Override
    default String getCode() {
        return getStringFieldValue("code");
    }
}
