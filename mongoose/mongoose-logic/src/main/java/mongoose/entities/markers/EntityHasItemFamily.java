package mongoose.entities.markers;

import mongoose.entities.ItemFamily;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

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
