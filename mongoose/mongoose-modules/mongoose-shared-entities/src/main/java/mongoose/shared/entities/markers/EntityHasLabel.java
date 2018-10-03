package mongoose.shared.entities.markers;

import mongoose.shared.entities.Label;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface EntityHasLabel extends Entity, HasLabel {

    @Override
    default void setLabel(Object label) {
        setForeignField("label", label);
    }

    @Override
    default EntityId getLabelId() {
        return getForeignEntityId("label");
    }

    @Override
    default Label getLabel() {
        return getForeignEntity("label");
    }

}
