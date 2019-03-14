package mongoose.shared.entities.markers;

import mongoose.shared.entities.ItemFamily;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface EntityHasItemFamily extends Entity, HasItemFamily {

    @Override
    default void setItemFamily(Object itemFamily) {
        setForeignField("itemFamily", itemFamily);
    }

    @Override
    default EntityId getItemFamilyId() {
        return getForeignEntityId("itemFamily");
    }

    @Override
    default ItemFamily getItemFamily() {
        return getForeignEntity("itemFamily");
    }
}
