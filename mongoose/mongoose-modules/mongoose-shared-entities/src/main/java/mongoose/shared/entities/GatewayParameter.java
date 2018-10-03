package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasName;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface GatewayParameter extends Entity, EntityHasName {

    default void setValue(String value) {
        setFieldValue("value", value);
    }

    default String getValue() {
        return getStringFieldValue("value");
    }
    
    default void setTest(Boolean test) {
        setFieldValue("test", test);
    }

    default Boolean isTest() {
        return getBooleanFieldValue("test");
    }

    default void setLive(Boolean live) {
        setFieldValue("live", live);
    }

    default Boolean isLive() {
        return getBooleanFieldValue("live");
    }

}
