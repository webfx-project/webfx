package mongooses.core.shared.entities;

import mongooses.core.shared.entities.markers.EntityHasCode;
import mongooses.core.shared.entities.markers.EntityHasLabel;
import mongooses.core.shared.entities.markers.EntityHasName;
import mongooses.core.shared.entities.markers.HasItemFamilyType;
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
