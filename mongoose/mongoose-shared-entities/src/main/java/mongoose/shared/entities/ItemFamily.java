package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasCode;
import mongoose.shared.entities.markers.EntityHasLabel;
import mongoose.shared.entities.markers.EntityHasName;
import mongoose.shared.entities.markers.HasItemFamilyType;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface ItemFamily extends Entity, EntityHasCode, EntityHasName, EntityHasLabel, HasItemFamilyType {

    @Override
    default ItemFamilyType getItemFamilyType() {
        return ItemFamilyType.fromCode(getCode());
    }
}
