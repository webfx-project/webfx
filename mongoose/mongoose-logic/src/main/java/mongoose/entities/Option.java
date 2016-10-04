package mongoose.entities;

import mongoose.entities.markers.*;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface Option extends Entity, EntityHasParent<Option>, EntityHasName, EntityHasLabel, EntityHasSite, EntityHasItem, HasItemFamilyType, EntityHasDateTimeRange {

    //// Domain fields

    default void setTimeRange(String timeRange) {
        setFieldValue("timeRange", timeRange);
    }

    default String getTimeRange() {
        return getStringFieldValue("timeRange");
    }

    default void setFolder(Boolean folder) {
        setFieldValue("folder", folder);
    }

    default Boolean isFolder() {
        return getBooleanFieldValue("folder");
    }

    default void setObligatory(Boolean obligatory) {
        setFieldValue("obligatory", obligatory);
    }

    default Boolean isObligatory() {
        return getBooleanFieldValue("obligatory");
    }

    default void setItemFamily(Object family) {
        setForeignField("itemFamily", family);
    }

    default EntityId getItemFamilyId() {
        return getForeignEntityId("itemFamily");
    }

    default ItemFamily getItemFamily() {
        return getForeignEntity("itemFamily");
    }

    //// Enriched fields and methods

    default Label bestLabel() {
        Label label = getLabel();
        if (label != null)
            return label;
        Item item = getItem();
        if (item != null)
            label = item.getLabel();
        return label;
    }

    @Override
    default ItemFamilyType getItemFamilyType() {
        Item item = getItem();
        if (item != null)
            return item.getItemFamilyType();
        ItemFamily itemFamily = getItemFamily();
        if (itemFamily != null)
            return itemFamily.getItemFamilyType();
        Option parent = getParent();
        return parent == null ? ItemFamilyType.UNKNOWN : parent.getItemFamilyType();
    }

    default boolean isConcrete() {
        return !isFolder() && getSite() != null && getItem() != null;
    }

    default boolean hasTimeRange() {
        return getTimeRangeOrParent() != null;
    }

    default String getTimeRangeOrParent() {
        return getStringFieldValueOrParent("timeRange");
    }

    default String getDateTimeRangeOrParent() {
        return getStringFieldValueOrParent("dateTimeRange");
    }
}
