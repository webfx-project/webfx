package mongooses.core.entities.markers;

import mongooses.core.entities.Item;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface EntityHasItem extends Entity, HasItem {

    @Override
    default void setItem(Object item) {
        setForeignField("item", item);
    }

    @Override
    default EntityId getItemId() {
        return getForeignEntityId("item");
    }

    @Override
    default Item getItem() {
        return getForeignEntity("item");
    }
}
