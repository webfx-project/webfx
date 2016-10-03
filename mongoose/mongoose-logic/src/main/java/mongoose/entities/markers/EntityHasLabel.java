package mongoose.entities.markers;

import mongoose.entities.Label;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

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
