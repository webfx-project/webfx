package mongooses.core.shared.entities.markers;

import mongooses.core.shared.entities.Item;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasItem {

    void setItem(Object item);

    EntityId getItemId();

    Item getItem();

    default boolean hasItem() {
        return getItem() != null;
    }

}
