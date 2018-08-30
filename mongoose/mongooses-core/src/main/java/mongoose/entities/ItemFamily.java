package mongoose.entities;

import mongoose.entities.markers.EntityHasCode;
import mongoose.entities.markers.EntityHasLabel;
import mongoose.entities.markers.EntityHasName;
import mongoose.entities.markers.HasItemFamilyType;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface ItemFamily extends Entity, EntityHasCode, EntityHasName, EntityHasLabel, HasItemFamilyType {

    @Override
    default ItemFamilyType getItemFamilyType() {
        return ItemFamilyType.fromCode(getCode());
    }
}
