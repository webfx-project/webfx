package mongooses.core.shared.entities;

import mongooses.core.shared.entities.markers.EntityHasCode;
import mongooses.core.shared.entities.markers.EntityHasLabel;
import mongooses.core.shared.entities.markers.EntityHasName;
import mongooses.core.shared.entities.markers.HasItemFamilyType;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface Item extends Entity, EntityHasCode, EntityHasName, EntityHasLabel, HasItemFamilyType {

    //// Domain fields

    default void setFamily(Object family) {
        setForeignField("family", family);
    }

    default EntityId getFamilyId() {
        return getForeignEntityId("family");
    }

    default ItemFamily getFamily() {
        return getForeignEntity("family");
    }

    default void setRateAliasItem(Object rateAliasItem) {
        setForeignField("rateAliasItem", rateAliasItem);
    }

    default EntityId getRateAliasItemId() {
        return getForeignEntityId("rateAliasItem");
    }

    default Item getRateAliasItem() {
        return getForeignEntity("rateAliasItem");
    }

    default void setShare_mate(Boolean share_mate) {
        setFieldValue("share_mate", share_mate);
    }

    default Boolean isShare_mate() {
        return getBooleanFieldValue("share_mate");
    }

    //// Enriched fields and methods

    @Override
    default ItemFamilyType getItemFamilyType() {
        ItemFamily family = getFamily();
        return family == null ? ItemFamilyType.UNKNOWN : family.getItemFamilyType();
    }
}
