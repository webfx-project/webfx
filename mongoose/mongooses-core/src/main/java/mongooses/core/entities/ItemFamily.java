package mongooses.core.entities;

import mongooses.core.entities.markers.EntityHasCode;
import mongooses.core.entities.markers.EntityHasLabel;
import mongooses.core.entities.markers.EntityHasName;
import mongooses.core.entities.markers.HasItemFamilyType;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface ItemFamily extends Entity, EntityHasCode, EntityHasName, EntityHasLabel, HasItemFamilyType {

    @Override
    default ItemFamilyType getItemFamilyType() {
        return ItemFamilyType.fromCode(getCode());
    }
}
