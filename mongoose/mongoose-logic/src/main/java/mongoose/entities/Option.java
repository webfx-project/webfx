package mongoose.entities;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.entities.markers.*;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public interface Option extends Entity,
        EntityHasParent<Option>,
        EntityHasEvent,
        EntityHasName,
        EntityHasLabel,
        EntityHasSiteAndItem,
        HasItemFamilyType,
        EntityHasDateTimeRange {

    //// Domain fields

    default void setForceSoldout(Boolean forceSoldout) {
        setFieldValue("forceSoldout", forceSoldout);
    }

    default Boolean isForceSoldout() {
        return getBooleanFieldValue("forceSoldout");
    }

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

    default void setFloating(Boolean floating) {
        setFieldValue("floating", floating);
    }

    default Boolean isFloating() {
        return getBooleanFieldValue("floating");
    }

    default void setTopLabel(Object topLabel) {
        setForeignField("topLabel", topLabel);
    }

    default EntityId getTopLabelId() {
        return getForeignEntityId("topLabel");
    }

    default Label getTopLabel() {
        return getForeignEntity("topLabel");
    }

    //// Enriched fields and methods

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
        return !isFolder() && hasSiteAndItem();
    }

    default boolean isIncludedByDefault() {
        return isConcrete() ||
                hasItem() && hasTimeRange(); // Ex: Prayers -> to include in the working document so it is displayed in the calendar
    }

    default boolean isBreakfast() {
        if (!isMeals())
            return false;
        DayTimeRange dayTimeRange = DayTimeRange.parse(getTimeRangeOrParent());
        return dayTimeRange != null && dayTimeRange.getDayTimeInterval(0, TimeUnit.DAYS).getExcludedEnd() < 10 * 60;
    }

    default boolean isDependant() {
        return isDiet() || isBreakfast();
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

    DayTimeRange getParsedTimeRangeOrParent();

    DateTimeRange getParsedDateTimeRangeOrParent();
}
